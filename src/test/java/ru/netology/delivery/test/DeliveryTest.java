package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;


import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        Configuration.holdBrowserOpen = true;
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        //fill form
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();

        //send form
        $(".button__text").click();

        //check
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText(("Встреча успешно запланирована на " + firstMeetingDate)));

        //send form with new date
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(".button__text").click();

        //check
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(exactText("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        //click
        ElementsCollection buttons = $$(".button__text");
        buttons.findBy(exactText("Перепланировать")).click();

        //check
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText(("Встреча успешно запланирована на " + secondMeetingDate)));
    }
}
