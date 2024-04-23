package com.gypApp_main.modelTest;

import com.gypApp_main.model.Roles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RolesTest {

    @Test
    public void testEquals() {
        Roles role1 = new Roles();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Roles role2 = new Roles();
        role2.setId(1);
        role2.setName("ROLE_USER");

        assertEquals(role1, role2);
    }

    @Test
    public void testHashCode() {
        Roles role1 = new Roles();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Roles role2 = new Roles();
        role2.setId(1);
        role2.setName("ROLE_USER");

        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testToString() {
        Roles role = new Roles();
        role.setId(1);
        role.setName("ROLE_USER");

        String expectedToString = "Roles(id=1, name=ROLE_USER)";
        assertEquals(expectedToString, role.toString());
    }

    @Test
    public void testGetters() {
        Roles role = new Roles();
        role.setId(1);
        role.setName("ROLE_USER");

        assertEquals(1, role.getId());
        assertEquals("ROLE_USER", role.getName());
    }
}
