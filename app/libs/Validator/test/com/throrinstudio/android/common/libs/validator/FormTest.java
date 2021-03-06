package com.throrinstudio.android.common.libs.validator;

import android.content.Context;
import android.widget.TextView;

import com.validator.Validate;
import com.validator.Form;
import com.validator.validator.EmailValidator;
import com.validator.validator.NotEmptyValidator;
import com.validator.validator.PhoneValidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Created by piobab on 03.04.2014.
 */
@RunWith(RobolectricGradleTestRunner.class)
public class FormTest {

    @Test
    public void validateFormWithoutAnyValidates() throws Exception {
        Form form = new Form();
        assertTrue(form.validate());
    }

    @Test
    public void validateFormWithNotEmptyAndEmailAndPhoneValidators() throws Exception {
        Context context = Robolectric.getShadowApplication().getApplicationContext();

        TextView notEmptyField = new TextView(context);
        notEmptyField.setText("validate");
        Validate notEmptyValidate = new Validate(notEmptyField);
        notEmptyValidate.addValidator(new NotEmptyValidator(context));

        TextView emailField = new TextView(context);
        emailField.setText("email@gmail.com");
        Validate emailValidate = new Validate(emailField);
        emailValidate.addValidator(new EmailValidator(context));

        TextView phoneField = new TextView(context);
        phoneField.setText("+46123456789");
        Validate phoneValidate = new Validate(phoneField);
        phoneValidate.addValidator(new PhoneValidator(context));

        Form form = new Form();
        form.addValidates(notEmptyValidate);
        form.addValidates(emailValidate);
        form.addValidates(phoneValidate);
        assertTrue(form.validate());
    }
}
