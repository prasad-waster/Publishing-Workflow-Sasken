package com.blogcraft;

import com.blogcraft.model.Draft;
import com.blogcraft.model.Comment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testDraftModel() {
        Draft draft = new Draft("Title", "Content", "Author");
        assertEquals("Title", draft.getTitle());
        assertEquals("Content", draft.getContent());
        assertEquals("Author", draft.getUsername());
    }

    @Test
    public void testCommentModel() {
        Comment comment = new Comment("User", "Nice work!");
        assertEquals("User", comment.getUsername());
        assertEquals("Nice work!", comment.getComment());
    }
}
