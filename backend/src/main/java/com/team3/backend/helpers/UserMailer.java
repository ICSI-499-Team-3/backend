package com.team3.backend.helpers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class UserMailer {
    private static final HashMap<String, String> emailConfig = new HashMap<>();

    static {
        try {
            String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\com\\team3\\backend\\helpers\\email.config.json";
            JSONObject emailConfigJSON = (JSONObject) new JSONParser().parse(new FileReader(filePath));

            emailConfig.put("name", (String) emailConfigJSON.get("name"));
            emailConfig.put("email", (String) emailConfigJSON.get("email"));
            emailConfig.put("password", (String) emailConfigJSON.get("password"));
            emailConfig.put("host", (String) emailConfigJSON.get("host"));
            emailConfig.put("port", (String) emailConfigJSON.get("port"));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    // Sends an email to the user either containing a recovery code to help reset his/her password if the user is valid,
    // or an informational email if they are not a valid user in the database
    public static void sendEmail(String email, String name, String resetCode, boolean validUser) {
        String body = validUser ? setEmailBody(resetCode, true) : setEmailBody("", false);

        // Build email object to be sent
        Email emailContent = EmailBuilder.startingBlank()
                .from(emailConfig.get("name"), emailConfig.get("email"))
                .to(name, email)
                .withSubject("Dyevo App Password Reset")
                .withHTMLText("<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "\n" +
                        "  <meta charset=\"utf-8\">\n" +
                        "  <meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n" +
                        "  <title>Password Reset</title>\n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                        "  <style type=\"text/css\">\n" +
                        "  /**\n" +
                        "   * Google webfonts. Recommended to include the .woff version for cross-client compatibility.\n" +
                        "   */\n" +
                        "  @media screen {\n" +
                        "    @font-face {\n" +
                        "      font-family: 'Source Sans Pro';\n" +
                        "      font-style: normal;\n" +
                        "      font-weight: 400;\n" +
                        "      src: local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff');\n" +
                        "    }\n" +
                        "\n" +
                        "    @font-face {\n" +
                        "      font-family: 'Source Sans Pro';\n" +
                        "      font-style: normal;\n" +
                        "      font-weight: 700;\n" +
                        "      src: local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff');\n" +
                        "    }\n" +
                        "  }\n" +
                        "\n" +
                        "  /**\n" +
                        "   * Avoid browser level font resizing.\n" +
                        "   * 1. Windows Mobile\n" +
                        "   * 2. iOS / OSX\n" +
                        "   */\n" +
                        "  body,\n" +
                        "  table,\n" +
                        "  td,\n" +
                        "  a {\n" +
                        "    -ms-text-size-adjust: 100%; /* 1 */\n" +
                        "    -webkit-text-size-adjust: 100%; /* 2 */\n" +
                        "  }\n" +
                        "\n" +
                        "  /**\n" +
                        "   * Remove extra space added to tables and cells in Outlook.\n" +
                        "   */\n" +
                        "  table,\n" +
                        "  td {\n" +
                        "    mso-table-rspace: 0pt;\n" +
                        "    mso-table-lspace: 0pt;\n" +
                        "  }\n" +
                        "\n" +
                        "  /**\n" +
                        "   * Better fluid images in Internet Explorer.\n" +
                        "   */\n" +
                        "  img {\n" +
                        "    -ms-interpolation-mode: bicubic;\n" +
                        "  }\n" +
                        "\n" +
                        "  /**\n" +
                        "   * Remove blue links for iOS devices.\n" +
                        "   */\n" +
                        "  a[x-apple-data-detectors] {\n" +
                        "    font-family: inherit !important;\n" +
                        "    font-size: inherit !important;\n" +
                        "    font-weight: inherit !important;\n" +
                        "    line-height: inherit !important;\n" +
                        "    color: inherit !important;\n" +
                        "    text-decoration: none !important;\n" +
                        "  }\n" +
                        "\n" +
                        "  /**\n" +
                        "   * Fix centering issues in Android 4.4.\n" +
                        "   */\n" +
                        "  div[style*=\"margin: 16px 0;\"] {\n" +
                        "    margin: 0 !important;\n" +
                        "  }\n" +
                        "\n" +
                        "  body {\n" +
                        "    width: 100% !important;\n" +
                        "    height: 100% !important;\n" +
                        "    padding: 0 !important;\n" +
                        "    margin: 0 !important;\n" +
                        "  }\n" +
                        "\n" +
                        "  /**\n" +
                        "   * Collapse table borders to avoid space between cells.\n" +
                        "   */\n" +
                        "  table {\n" +
                        "    border-collapse: collapse !important;\n" +
                        "  }\n" +
                        "\n" +
                        "  a {\n" +
                        "    color: #1a82e2;\n" +
                        "  }\n" +
                        "\n" +
                        "  img {\n" +
                        "    height: auto;\n" +
                        "    line-height: 100%;\n" +
                        "    text-decoration: none;\n" +
                        "    border: 0;\n" +
                        "    outline: none;\n" +
                        "  }\n" +
                        "  </style>\n" +
                        "\n" +
                        "</head>\n" +
                        "<body style=\"background-color: #e9ecef;\">\n" +
                        "\n" +
                        "  <!-- start preheader -->\n" +
                        "  <div class=\"preheader\" style=\"display: none; max-width: 0; max-height: 0; overflow: hidden; font-size: 1px; line-height: 1px; color: #fff; opacity: 0;\">\n" +
                        "    \n" +
                        "  </div>\n" +
                        "  <!-- end preheader -->\n" +
                        "\n" +
                        "  <!-- start body -->\n" +
                        body +
                        "  <!-- end body -->\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>")
                .buildEmail();

        // Build Mailer with SMTP server details and email credentials
        Mailer mailer = MailerBuilder
                .withSMTPServer(
                        emailConfig.get("host"),
                        Integer.valueOf(emailConfig.get("port")),
                        emailConfig.get("email"),
                        emailConfig.get("password"))
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();

        mailer.sendMail(emailContent);
    }

    // Sets the body of the email to either include a recovery code, or inform the recipient that they are not in our database
    private static String setEmailBody(String passwordResetCode, boolean validUser) {
        String body;

        if (validUser) {
            body = "  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "\n" +
                    "    <!-- start hero -->\n" +
                    "    <tr>\n" +
                    "      <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                    "        <tr>\n" +
                    "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                    "        <![endif]-->\n" +
                    "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                    "          <tr>\n" +
                    "            <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf;\">\n" +
                    "              <h1 style=\"margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;\">Reset Your Password</h1>\n" +
                    "            </td>\n" +
                    "          </tr>\n" +
                    "        </table>\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        </td>\n" +
                    "        </tr>\n" +
                    "        </table>\n" +
                    "        <![endif]-->\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    <!-- end hero -->\n" +
                    "\n" +
                    "    <!-- start copy block -->\n" +
                    "    <tr>\n" +
                    "      <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                    "        <tr>\n" +
                    "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                    "        <![endif]-->\n" +
                    "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                    "\n" +
                    "          <!-- start copy -->\n" +
                    "          <tr>\n" +
                    "            <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n" +
                    "              <p style=\"margin: 0;\">Use the code below to reset your Dyevo app password." +
                    "               The code will expire in 60 minutes, or after it is used to reset your password. If you didn't request this change, you can safely ignore this email.</p>\n" +
                    "            </td>\n" +
                    "          </tr>\n" +
                    "          <!-- end copy -->\n" +
                    "\n" +
                    "          <!-- start button -->\n" +
                    "          <tr>\n" +
                    "            <td align=\"left\" bgcolor=\"#ffffff\">\n" +
                    "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                <tr>\n" +
                    "                  <td align=\"center\" bgcolor=\"#ffffff\" style=\"padding: 12px;\">\n" +
                    "                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "                      <tr>\n" +
                    "                        <td align=\"center\" bgcolor=\"#1a82e2\" style=\"border-radius: 6px;\">\n" +
                    "                          <a style=\"display: inline-block; padding: 16px 36px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 24px; color: #ffffff; text-decoration: none; border-radius: 6px;\">"
                    + passwordResetCode + "</a>\n" +
                    "                        </td>\n" +
                    "                      </tr>\n" +
                    "                    </table>\n" +
                    "                  </td>\n" +
                    "                </tr>\n" +
                    "              </table>\n" +
                    "            </td>\n" +
                    "          </tr>\n" +
                    "          <!-- end button -->\n" +
                    "\n" +
                    "          <!-- start copy -->\n" +
                    "          <tr>\n" +
                    "            <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; border-bottom: 3px solid #d4dadf\">\n" +
                    "              <p style=\"margin: 0;\">Best,<br> Dyevo App Support Team</p>\n" +
                    "            </td>\n" +
                    "          </tr>\n" +
                    "          <!-- end copy -->\n" +
                    "\n" +
                    "        </table>\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        </td>\n" +
                    "        </tr>\n" +
                    "        </table>\n" +
                    "        <![endif]-->\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    <!-- end copy block -->\n" +
                    "\n" +
                    "    <!-- start footer -->\n" +
                    "    <tr>\n" +
                    "      <td align=\"center\" bgcolor=\"#e9ecef\" style=\"padding: 24px;\">\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                    "        <tr>\n" +
                    "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                    "        <![endif]-->\n" +
                    "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                    "\n" +
                    "          <!-- start permission -->\n" +
                    "          <tr>\n" +
                    "            <td align=\"center\" bgcolor=\"#e9ecef\" style=\"padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;\">\n" +
                    "              <p style=\"margin: 0;\">You received this email because we received a request for a password reset for your account. If you didn't request a password reset you can safely ignore this email.</p>\n" +
                    "            </td>\n" +
                    "          </tr>\n" +
                    "          <!-- end permission -->\n" +
                    "\n" +
                    "        </table>\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        </td>\n" +
                    "        </tr>\n" +
                    "        </table>\n" +
                    "        <![endif]-->\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    <!-- end footer -->\n" +
                    "\n" +
                    "  </table>\n";
        } else {
            body = "  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "\n" +
                    "    <!-- start hero -->\n" +
                    "    <tr>\n" +
                    "      <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                    "        <tr>\n" +
                    "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                    "        <![endif]-->\n" +
                    "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                    "          <tr>\n" +
                    "            <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf;\">\n" +
                    "              <h1 style=\"margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;\">Invalid Account</h1>\n" +
                    "            </td>\n" +
                    "          </tr>\n" +
                    "        </table>\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        </td>\n" +
                    "        </tr>\n" +
                    "        </table>\n" +
                    "        <![endif]-->\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    <!-- end hero -->\n" +
                    "\n" +
                    "    <!-- start copy block -->\n" +
                    "    <tr>\n" +
                    "      <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                    "        <tr>\n" +
                    "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                    "        <![endif]-->\n" +
                    "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                    "\n" +
                    "          <!-- start copy -->\n" +
                    "          <tr>\n" +
                    "            <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n" +
                    "              <p style=\"margin: 0;\">We received your request to reset your password. But, there is no Dyevo app account linked with your email address. If you didn't make this request, you can safely ignore this email.</p>\n" +
                    "            </td>\n" +
                    "          </tr>\n" +
                    "          <!-- end copy -->\n" +
                    "\n" +
                    "          <!-- start copy -->\n" +
                    "          <tr>\n" +
                    "            <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; border-bottom: 3px solid #d4dadf\">\n" +
                    "              <p style=\"margin: 0;\">Best,<br> Dyevo App Support Team</p>\n" +
                    "            </td>\n" +
                    "          </tr>\n" +
                    "          <!-- end copy -->\n" +
                    "\n" +
                    "        </table>\n" +
                    "        <!--[if (gte mso 9)|(IE)]>\n" +
                    "        </td>\n" +
                    "        </tr>\n" +
                    "        </table>\n" +
                    "        <![endif]-->\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    <!-- end copy block -->\n" +
                    "\n" +
                    "  </table>\n";
        }

        return body;
    }
}
