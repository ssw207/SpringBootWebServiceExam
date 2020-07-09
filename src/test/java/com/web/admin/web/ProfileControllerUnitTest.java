package com.web.admin.web;

import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileControllerUnitTest {

    @Test
    public void real_profile이_조회된다() {
        //given
        String expectedProfile = "real";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        //when
        ProfileController controller = new ProfileController(env);
        String returnProfile = controller.profile();

        //then
        assertThat(returnProfile).isEqualTo(expectedProfile);
    }

    @Test
    public void real_profile이_없으면_첫번쨰_profile이_조회된다() {
        //given
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");

        //when
        ProfileController controller = new ProfileController(env);
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void active_profile이_없으면_default가_조회된다() {
        String expectedProfile= "default";
        //given
        MockEnvironment env = new MockEnvironment();

        //when
        ProfileController controller = new ProfileController(env);
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}
