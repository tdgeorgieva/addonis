package com.addonis.modelMappers;

import com.addonis.dtos.AddonDto;
import com.addonis.exceptions.InvalidInputException;
import com.addonis.models.addon.Addon;
import com.addonis.models.addon.AddonStatus;
import com.addonis.models.addon.AddonTag;
import com.addonis.repositories.IDE.IDERepository;
import com.addonis.repositories.addon.AddonRepository;
import com.addonis.repositories.addon.TagRepository;
import com.addonis.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Component
public class AddonModelMapper {

    private final AddonRepository addonRepository;
    private final UserRepository userRepository;
    private final IDERepository ideRepository;
    private final TagRepository tagRepository;

    @Autowired
    public AddonModelMapper(AddonRepository addonRepository, UserRepository userRepository, IDERepository ideRepository, TagRepository tagRepository) {
        this.addonRepository = addonRepository;
        this.userRepository = userRepository;
        this.ideRepository = ideRepository;
        this.tagRepository = tagRepository;
    }

    public Addon fromDto(AddonDto addonDto, boolean isDraft) throws IOException {
        Addon addon = new Addon();
        if (!isDraft) {
            if (!validInputForCreation(addonDto)) {
                throw new InvalidInputException();
            }
            addon.setStatus(AddonStatus.PENDING);
        } else {
            addon.setStatus(AddonStatus.DRAFT);
        }
        dtoToCreateObject(addonDto, addon);
        return addon;
    }


    public Addon fromDto(AddonDto addonDto, int id, boolean isDraft) throws IOException {
        Addon addon = addonRepository.getById(id);

        if (isDraft) {
            addon.setStatus(AddonStatus.DRAFT);
        } else {
            if (!validInputForCreation(addonDto)) {
                throw new InvalidInputException();
            }
            if (addonDto.getStatus() == AddonStatus.DRAFT) {
                addon.setStatus(AddonStatus.PENDING);
            } else {
                addon.setStatus(addonDto.getStatus());
            }
        }
        // fill data
        List<AddonTag> list = tagRepository.getAll();
        Map<Integer, String> map = new HashMap<>();
        for (AddonTag tag: list) {
            map.put(tag.getId(), tag.getName());
        }
        Set<AddonTag> tags = new HashSet<>();
        for (String tagIdStr : addonDto.getTags()) {
            int tagID = Integer.parseInt(tagIdStr);
            if (map.containsKey(tagID)) {
                String tagName = map.get(tagID);
                tags.add(new AddonTag(tagID, tagName));
            }
        }

        addon.setTags(tags);
        dtoToUpdateObject(addonDto, addon);

        return addon;
    }

    private boolean validInputForCreation(AddonDto dto) {
        return !dto.getDescription().equals("")
                && !dto.getLink().equals("")
                && !dto.getName().equals("");
    }

    private void dtoToCreateObject(AddonDto addonDto, Addon addon) throws IOException {
        addon.setName(addonDto.getName());
        addon.setDescription(addonDto.getDescription());
        addon.setIde(ideRepository.getById(addonDto.getIdeId()));
        addon.setDownloadsCount(0);
        addon.setDate(LocalDate.now());
        addon.setLink(addonDto.getLink());
        addon.setFeatured(false);
        if (addonDto.getFile() != null && !addonDto.getFile().isEmpty()) {
            addon.setImage(addonDto.getFile().getBytes());
        }
        if (addonDto.getBinaryContent() != null && !addonDto.getBinaryContent().isEmpty()) {
            Path path = Paths.get("src/main/resources/static/assets/addons//" + addon.getId());
            Files.write(path, addonDto.getBinaryContent().getBytes());
        }

    }

    private void dtoToUpdateObject(AddonDto addonDto, Addon addon) throws IOException {
        addon.setName(addonDto.getName());
        addon.setDescription(addonDto.getDescription());
        if (addonDto.getIdeId() != -1) {
            addon.setIde(ideRepository.getById(addonDto.getIdeId()));
        }
//        addon.setType(addonDto.getType());
        addon.setLink(addonDto.getLink());
        if (addonDto.getFile() != null && !addonDto.getFile().isEmpty()) {
            addon.setImage(addonDto.getFile().getBytes());
        }
        if (addonDto.getBinaryContent() != null && !addonDto.getBinaryContent().isEmpty()) {
            Path path = Paths.get("src/main/resources/static/assets/addons//" + addon.getId());
            Files.write(path, addonDto.getBinaryContent().getBytes());
        }
    }

    public AddonDto toDto(Addon addon) {
        AddonDto addonDto = new AddonDto();
        addonDto.setName(addon.getName());
        addonDto.setDescription(addon.getDescription());
        addonDto.setLink(addon.getLink());
        addonDto.setIdeId(addon.getId());
        addonDto.setFile(addonDto.getFile());

        addonDto.setUserId(addon.getUser().getId());
        addonDto.setStatus(addon.getStatus());
        addonDto.setDownloadLink(addon.getLink());

        addonDto.setFile(convertUserImageToMultipartFile(addon));
        return addonDto;
    }

    private MultipartFile convertUserImageToMultipartFile(Addon addon) {
        File file = new File("images/users/multipartFileImage");
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", addon.getImage());
        return multipartFile;
    }
}
