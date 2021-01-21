package com.webapp.microservice.controller;

import java.util.ArrayList;
import java.util.List;

import com.webapp.microservice.form.CharacterForm;
import com.webapp.microservice.model.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {

    @Autowired
    private RestTemplate restTemplate;

//    private static final List<Character> characters = new ArrayList<>();
//
//    static {
//        characters.add(new Character(1, "Gandalf", "Mage"));
//        characters.add(new Character(2, "Aragorn", "Warrior"));
//    }

    // Injectez (inject) via application.properties.
    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @GetMapping(value = { "/", "/index" })
    public String index(Model model) {

        model.addAttribute("message", message);

        return "index";
    }

    @GetMapping(value = { "/characterList" })
    public List<Character> characterList(Model model) {
        String url = "http://localhost:8081/Characters";
        List<Character> characterList = restTemplate.getForObject(url, List.class);
        model.addAttribute("characterList", characterList);

        return characterList;
    }

    @GetMapping(value = { "/addCharacter" })
    public String showAddCharacterPage(Model model) {

        CharacterForm CharacterForm = new CharacterForm();
        model.addAttribute("characterForm", CharacterForm);

        return "addCharacter";
    }

    @PostMapping(value = { "/addCharacter" })
    public String saveCharacter(Model model, //
                             @ModelAttribute("characterForm") CharacterForm characterForm) {

        int id = characterForm.getId();
        String name = characterForm.getNom();
        String type = characterForm.getType();
        String url = "http://localhost:8081/Characters";

        if (name != null && name.length() > 0 //
                && type != null && type.length() > 0) {
            restTemplate.postForObject(url, characterForm, Character.class);
//            Character newCharacter = new Character(id, name, type);
//            characters.add(newCharacter);

            return "redirect:/characterList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCharacter";
    }

}
