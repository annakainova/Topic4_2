package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
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
        $("[data-test-id='replan-notification'] .notification__content").should(appear, Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        //click
        ElementsCollection buttons = $$(".button__text");
        buttons.findBy(exactText("Перепланировать")).click();

        //check
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText(("Встреча успешно запланирована на " + secondMeetingDate)));
    }

    @Test
    public void sendFormCityNotFromListTest() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var date = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").setValue(DataGenerator.generateCityNotFromList());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button__text").click();
        $x("//span[@data-test-id='city']//span[@class='input__sub'][contains(text(), 'Доставка в выбранный город недоступна')]").should(appear);
    }

    @Test
    public void sendFormEmptyCityTest() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var date = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button__text").click();
        $x("//span[@data-test-id='city']//span[@class='input__sub'][contains(text(), 'Поле обязательно для заполнения')]").should(appear);
    }

    @Test
    public void sendFormEmptyDateTest() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button__text").click();
        $x("//span[@data-test-id='date']//span[@class='input__sub'][contains(text(), 'Неверно введена дата')]").should(appear);
    }

    @Test
    public void sendFormDateEarlierThan3DaysTest() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 2;
        var date = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button__text").click();
        $x("//span[@data-test-id='date']//span[@class='input__sub'][contains(text(), 'Заказ на выбранную дату невозможен')]").should(appear);
    }

    @Test
    public void sendFormEmptyNameTest() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var date = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button__text").click();
        $x("//span[@data-test-id='name']//span[@class='input__sub'][contains(text(), 'Поле обязательно для заполнения')]").should(appear);
    }

    @Test
    public void sendFormLatinInNameTest() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var date = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateName("en"));
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button__text").click();
        $x("//span[@data-test-id='name']//span[@class='input__sub'][contains(text(), 'Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.')]").should(appear);
    }

    @Test
    public void sendFormEmptyPhoneTest() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var date = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='agreement']").click();
        $(".button__text").click();
        $x("//span[@data-test-id='phone']//span[@class='input__sub'][contains(text(), 'Поле обязательно для заполнения')]").should(appear);
    }

    @Test
    public void sendFormEmptyCheckBoxTest() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var date = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL,"a",Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $(".button__text").click();
        $("[data-test-id='agreement'].input_invalid").shouldBe(enabled);
        $x("//div[contains(text(), 'Успешно!')]").shouldNot(appear, Duration.ofSeconds(15));
    }



}
