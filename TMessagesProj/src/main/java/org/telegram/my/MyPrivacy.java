package org.telegram.my;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.google.android.gms.analytics.HitBuilders;

import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * Created by Mostafa on 14/02/2016.
 */
public class MyPrivacy {
//    public synchronized static int Chat_CheckPermissionResult(int chatId) {
//        if (_Chat_CheckPermission_Cache != null && _Chat_CheckPermission_Cache.containsKey(Integer.valueOf(chatId))) {
//            return (_Chat_CheckPermission_Cache.get(Integer.valueOf(chatId)));
//        } else
//            return Chat_CheckPermission_Result_NotSetYet;
//    }
//
//    private static ArrayList<Integer> _Chat_CheckPermission_Processing = new ArrayList<>();
//
//    public static void Chat_CheckPermission(final Context context, final int chatId, final Semaphore semaphore) {
//        Utilities.globalQueue.postRunnable(new Runnable() {
//            @Override
//            public void run() {
//                if (_Chat_CheckPermission_Processing.contains(chatId)) {
//                    Chat_CheckPermission_SaveResult(chatId, Chat_CheckPermission_Result_Unknown, null, false, false, false);
//                    return;
//                }
//                if (_Chat_CheckPermission_Cache != null && _Chat_CheckPermission_Cache.containsKey(Integer.valueOf(chatId))) {
//                    Chat_CheckPermission_SaveResult(chatId, _Chat_CheckPermission_Cache.get(Integer.valueOf(chatId)), semaphore, false, false, true);
//                    return;
//                }
//                _Chat_CheckPermission_Processing.add(chatId);
//
//                SQLiteDatabase db = MyDB.DB_Read(context);
//                Cursor cursor = db.rawQuery("SELECT " + MyDB.COLUMN_PERMISSION_CHECKS_VALID_IS + " FROM " + MyDB.TABLE_PERMISSION_CHECKS + " WHERE " + MyDB.COLUMN_PERMISSION_CHECKS_ID + "=?"
//                        , new String[]{String.valueOf(chatId)});
//                if (cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    Chat_CheckPermission_SaveResult(chatId, cursor.getInt(0), semaphore, true, false, true);
//                    cursor.close();
//                    return;
//                } else {
//                    cursor.close();
//                    if (chatId > 0) {
//                        final TLRPC.Chat finalChat = MessagesController.getInstance().getChat(chatId);
//                        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
//                        int my_PreventAutoAddByUnknownContact = preferences.getInt("my_PreventAutoAddByUnknownContact", 0);
//                        if (my_PreventAutoAddByUnknownContact <= 0 || finalChat.date - my_PreventAutoAddByUnknownContact <= 0) {
//                            Chat_CheckPermission_SaveResult(chatId, Chat_CheckPermission_Result_BeforeSettingDate, semaphore, true, true, true);
//                            return;
//                        }
//
//                        if (ChatObject.isChannel(chatId)) {
//                            TLRPC.TL_channels_getParticipant req = new TLRPC.TL_channels_getParticipant();
//                            req.channel = MessagesController.getInputChannel(chatId);
//                            req.user_id = new TLRPC.TL_inputUserSelf();
//                            ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
//                                @Override
//                                public void run(TLObject response, TLRPC.TL_error error) {
//                                    final TLRPC.TL_channels_channelParticipant res = (TLRPC.TL_channels_channelParticipant) response;
//                                    int inviterId = 0;
//                                    if (res != null && res.participant instanceof TLRPC.TL_channelParticipantSelf) {
//                                        inviterId = res.participant.inviter_id;
//                                    }
//                                    Chat_CheckPermission_InviterReceived(context, chatId, inviterId, false, semaphore, "");
//                                }
//                            });
//                        } else {
//                            TLRPC.TL_messages_getFullChat req = new TLRPC.TL_messages_getFullChat();
//                            req.chat_id = chatId;
//                            ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
//                                @Override
//                                public void run(TLObject response, final TLRPC.TL_error error) {
//                                    int inviterId = 0;
//                                    String chatText = "";
//                                    try {
//                                        if (error == null) {
//                                            final TLRPC.TL_messages_chatFull res = (TLRPC.TL_messages_chatFull) response;
//                                            if (finalChat != null && finalChat.megagroup) {
//                                                res.full_chat.unread_important_count = Math.max(res.full_chat.unread_important_count, res.full_chat.unread_count);
//                                            }
//                                            MessagesStorage.getInstance().putUsersAndChats(res.users, res.chats, true, true);
//                                            MessagesStorage.getInstance().updateChatInfo(res.full_chat, false);
//                                            for (TLRPC.ChatParticipant feP :
//                                                    res.full_chat.participants.participants) {
//                                                if (feP.user_id == UserConfig.getClientUserId()) {
//                                                    inviterId = feP.inviter_id;
//                                                    break;
//                                                }
//                                            }
//                                            chatText = res.full_chat.about;
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    Chat_CheckPermission_InviterReceived(context, chatId, inviterId, false, semaphore, chatText);
//                                }
//                            });
//                        }
//                    } else {
//                        Chat_CheckPermission_SaveResult(chatId, Chat_CheckPermission_Result_Unknown, semaphore, false, false, false);
//                    }
//                }
//            }
//        });
//    }
//
//    private static void Chat_CheckPermission_InviterReceived(final Context context
//            , final int chatId
//            , final int inviterId
//            , final boolean isOld
//            , final Semaphore semaphore
//            , final String chatText) {
//        new AsyncTask<String, Void, String>() {
//
//            @Override
//            protected String doInBackground(String... params) {
//                if (inviterId > 0 && inviterId != UserConfig.getClientUserId()) {
//                    ContactsController.getInstance().readContacts();
//                    while (ContactsController.getInstance().isLoadingContacts()) {
//                        try {
//                            wait(500);
//                        } catch (Exception e) {
//                        }
//                    }
//                }
//                return "1";
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                if (inviterId > 0) {
//                    boolean inviterIsInContacts = false;
//                    if (inviterId != UserConfig.getClientUserId()) {
//                        for (TLRPC.TL_contact feContact :
//                                ContactsController.getInstance().contacts) {
//                            if (feContact.user_id == inviterId) {
//                                inviterIsInContacts = true;
//                                break;
//                            }
//                        }
//                    } else
//                        inviterIsInContacts = true;
//                    Chat_CheckPermission_SaveResult(chatId, (inviterIsInContacts) ? Chat_CheckPermission_Result_Valid : Chat_CheckPermission_Result_InValid, semaphore, true, true, true);
//
//                    if (!inviterIsInContacts) {
//                        MessagesController.getInstance().deleteUserFromChat(chatId, UserConfig.getCurrentUser(), null);
//                        ApplicationLoader.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
//                                .setCategory(MyAnalytics.EVENTS.PRIVACY.Category)
//                                .setAction(MyAnalytics.EVENTS.PRIVACY.Blocked + " - " + String.valueOf(inviterId))
//                                .setLabel(chatText)
//                                .setValue(1)
//                                .build());
//                    }
//                } else {
//                    Chat_CheckPermission_SaveResult(chatId, Chat_CheckPermission_Result_Unknown, semaphore, true, true, true);
//                }
//            }
//        }.execute("");
//    }
//
//    public final static int Chat_CheckPermission_Result_Valid = 1;
//    public final static int Chat_CheckPermission_Result_InValid = 0;
//    public final static int Chat_CheckPermission_Result_Unknown = -1;
//    public final static int Chat_CheckPermission_Result_BeforeSettingDate = 2;
//    public final static int Chat_CheckPermission_Result_NotSetYet = 3;
//
//    private static HashMap<Integer, Integer> _Chat_CheckPermission_Cache = new HashMap<>();
//
//    private static synchronized void Chat_CheckPermission_SaveResult(int chatId, int result, Semaphore semaphore, boolean saveCache, boolean saveDB, boolean isFinished) {
//        if (saveCache) {
//            _Chat_CheckPermission_Cache.put(chatId, result);
//            _Chat_CheckPermission_Processing.remove(Integer.valueOf(chatId));
//        }
//        if (saveDB) {
//            SQLiteDatabase dbWrite = MyDB.DB_Write(ApplicationLoader.applicationContext);
//            ContentValues CVs = new ContentValues();
//            CVs.put(MyDB.COLUMN_PERMISSION_CHECKS_ID, chatId);
//            CVs.put(MyDB.COLUMN_PERMISSION_CHECKS_VALID_IS, result);
//            dbWrite.insert(MyDB.TABLE_PERMISSION_CHECKS, null, CVs);
//        }
//        if (isFinished)
//            _Chat_CheckPermission_Processing.remove(Integer.valueOf(chatId));
//
//        if (semaphore != null)
//            semaphore.release();
//    }

}
