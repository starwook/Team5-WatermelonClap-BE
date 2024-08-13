package com.watermelon.server.event.parts.dto.response;

import com.watermelon.server.event.parts.domain.Parts;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePartsListDto {

    private String category;
    private Long partsId;
    private String name;
    private String description;
    private String imgSrc;
    private String thumbnailImgSrc;
    private boolean isEquipped;

    public static ResponsePartsListDto any(){
        return ResponsePartsListDto.builder()
                .partsId(1L)
                .category("any")
                .name("any")
                .description("any")
                .imgSrc("any")
                .thumbnailImgSrc("any")
                .isEquipped(false)
                .build();
    }

    public static ResponsePartsListDto from(Parts parts, boolean isEquipped){
        return ResponsePartsListDto.builder()
                .category(parts.getCategory().toString())
                .partsId(parts.getId())
                .name(parts.getName())
                .description(parts.getDescription())
                .imgSrc(parts.getImgSrc())
                .thumbnailImgSrc(parts.getThumbnailImgSrc())
                .isEquipped(isEquipped)
                .build();
    }

}
