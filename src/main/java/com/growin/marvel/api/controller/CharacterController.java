package com.growin.marvel.api.controller;

import com.growin.marvel.api.dto.response.CharacterDataResponse;
import com.growin.marvel.api.service.ICharacterService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/characters")
@Tag(name = "character")
public class CharacterController {
    private final ICharacterService characterService;

    @GetMapping()
    @ApiResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Integer.class))))
    public Iterable<Integer> getIds() {
        return characterService.getAllIds();
    }

    @GetMapping("/{id}")
    public CharacterDataResponse getCharacterById(
            @PathVariable Integer id,
            @RequestParam(required = false) String language) throws Exception {
        return characterService.getCharacterById(id,language);
    }

}
