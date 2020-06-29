package com.web.admin.web;

import com.web.admin.config.auth.LoginUser;
import com.web.admin.config.auth.dto.SessionUser;
import com.web.admin.web.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;
    //private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts",postsService.findAllDesc());
        //SessionUser user = (SessionUser)httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName",user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsSave(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post",postsService.get(id));
        return "posts-update";
    }
}
