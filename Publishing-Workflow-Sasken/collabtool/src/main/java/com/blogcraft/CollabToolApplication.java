package com.blogcraft;

import com.blogcraft.model.Comment;
import com.blogcraft.model.Draft;
import com.blogcraft.repository.CommentRepository;
import com.blogcraft.repository.DraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CollabToolApplication implements CommandLineRunner {

    @Autowired
    private DraftRepository draftRepository;

    @Autowired
    private CommentRepository commentRepository;

    public static void main(String[] args) {
        SpringApplication.run(CollabToolApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("BlogCraft Collaboration Tool started...");

        Draft draft = new Draft("My First Draft", "This is a test draft.", "Vishakha");
        draftRepository.save(draft);

        Comment comment = new Comment("Anjali", "Great start! Add more details.");
        commentRepository.save(comment);

        System.out.println("Draft and comment added to DB!");
    }
}
