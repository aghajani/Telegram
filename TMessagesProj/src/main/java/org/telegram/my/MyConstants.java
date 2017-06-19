package org.telegram.my;

import android.app.Activity;
import android.content.SharedPreferences;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.MessagesController;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Mostafa on 25/01/2016.
 */
public class MyConstants {
    public static String Language_Get() {
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
        return preferences.getString("language", "");
    }

    public static Calendar Calendar_Localized(TimeZone timeZone, Locale locale) {
        Calendar r = null;
        if (Language_Get().equalsIgnoreCase("fa") ||
                Language_Get().equalsIgnoreCase("ku") ||
                Language_Get().equalsIgnoreCase("tr")) {
            r = new MyJalaliCalendar(timeZone, locale);
        } else {
            r = Calendar.getInstance(timeZone, locale);
        }
        return r;
    }

    public static TLObject Dialog_GetSender(long dialog_id) {
        TLRPC.User user = null;
        TLRPC.Chat chat = null;
        TLRPC.EncryptedChat encryptedChat = null;

        int r_Id_User = 0;
        int r_Id_Chat = 0;
        int r_Id_EncChat = 0;

        int lower_part = (int) dialog_id;
        int high_id = (int) (dialog_id >> 32);
        if (lower_part != 0) {
            if (high_id == 1) {
                r_Id_Chat = lower_part;
            } else {
                if (lower_part > 0) {
                    r_Id_User = lower_part;
                } else if (lower_part < 0) {
                    r_Id_Chat = -lower_part;
                }
            }
        } else {
            r_Id_EncChat = high_id;
        }

        if (r_Id_User > 0)
            user = MessagesController.getInstance().getUser(r_Id_User);
        else if (r_Id_Chat > 0)
            chat = MessagesController.getInstance().getChat(r_Id_Chat);
        else if (r_Id_EncChat > 0) {
            encryptedChat = MessagesController.getInstance().getEncryptedChat(r_Id_EncChat);
            if (encryptedChat != null) {
                user = MessagesController.getInstance().getUser(encryptedChat.user_id);
            }
        }

//        int lower_id = (int) dialogId;
//        int high_id = (int) (dialogId >> 32);
//        if (lower_id != 0) {
//            if (high_id == 1) {
//                chat = MessagesController.getInstance().getChat(lower_id);
//            } else {
//                if (lower_id < 0) {
//                    chat = MessagesController.getInstance().getChat(-lower_id);
//                    if (chat != null && chat.migrated_to != null) {
//                        TLRPC.Chat chat2 = MessagesController.getInstance().getChat(chat.migrated_to.channel_id);
//                        if (chat2 != null) {
//                            chat = chat2;
//                        }
//                    }
//                } else {
//                    user = MessagesController.getInstance().getUser(lower_id);
//                }
//            }
//        } else {
//            encryptedChat = MessagesController.getInstance().getEncryptedChat(high_id);
//            if (encryptedChat != null) {
//                user = MessagesController.getInstance().getUser(encryptedChat.user_id);
//            }
//        }

        TLObject r = null;
        if (user != null)
            r = user;
        else if (chat != null)
            r = chat;
//        if (encryptedChat != null)
//            r = encryptedChat;

        return r;
    }

    public static int Dialog_GetSenderId(long dialogId) {
        TLObject object = Dialog_GetSender(dialogId);
        if (object instanceof TLRPC.User)
            return ((TLRPC.User) object).id;
        if (object instanceof TLRPC.Chat)
            return ((TLRPC.Chat) object).id;
        if (object instanceof TLRPC.EncryptedChat)
            return ((TLRPC.EncryptedChat) object).id;
        else
            return 0;
    }
}
