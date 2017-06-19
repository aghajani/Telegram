package org.telegram.my;

import android.util.Pair;

import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.ui.ActionBar.ActionBarMenuItem;

import java.util.ArrayList;

/**
 * Created by Mostafa on 13/02/2016.
 */
public class MyFilter {

    public static int Filter_Mode_ResId_Icon(MyFilterModes filterMode, boolean isBlack) {
        int r = 0;
        switch (filterMode) {
            case Favourite:
                if (isBlack)
                    r = R.drawable.ic_stars_black_24dp;
                else
                    r = R.drawable.ic_stars_white_24dp;
                break;
            case Person:
                if (isBlack)
                    r = R.drawable.ic_person_black_24dp;
                else
                    r = R.drawable.ic_person_white_24dp;
                break;
            case Person_Online:
                if (isBlack)
                    r = R.drawable.ic_account_circle_black_24dp;
                else
                    r = R.drawable.ic_account_circle_white_24dp;
                break;
            case Group:
                if (isBlack)
                    r = R.drawable.ic_group_black_24dp;
                else
                    r = R.drawable.ic_group_white_24dp;
                break;
            case Channel:
                if (isBlack)
                    r = R.drawable.broadcast_b;
                else
                    r = R.drawable.broadcast_w;
                break;
            case All:
            default:
                if (isBlack)
                    r = R.drawable.ic_filter_list_black_24dp;
                else
                    r = R.drawable.ic_filter_list_white_24dp;
                break;
        }
        return r;
    }

    public static int Filter_Mode_ResId_String(MyFilterModes filterMode) {
        int r = 0;
        switch (filterMode) {
            case Favourite:
                r = R.string.My_FilterMode_Favourite;
                break;
            case Person:
                r = R.string.My_FilterMode_Person;
                break;
            case Person_Online:
                r = R.string.My_FilterMode_Person_Online;
                break;
            case Group:
                r = R.string.My_FilterMode_Group;
                break;
            case Channel:
                r = R.string.My_FilterMode_Channel;
                break;
            case All:
            default:
                r = R.string.My_FilterMode_All;
                break;
        }
        return r;
    }

    private static ArrayList<Pair<Integer, MyFilterModes>> _FilterModes_All = new ArrayList<>();

    public static ArrayList<Pair<Integer, MyFilterModes>> FilterModes_All() {
        if (_FilterModes_All.size() == 0) {
            int itemId = 0;

            itemId++;
            _FilterModes_All.add(new Pair<Integer, MyFilterModes>(itemId, MyFilterModes.All));

            itemId++;
            _FilterModes_All.add(new Pair<Integer, MyFilterModes>(itemId, MyFilterModes.Favourite));

            itemId++;
            _FilterModes_All.add(new Pair<Integer, MyFilterModes>(itemId, MyFilterModes.Person));

            itemId++;
            _FilterModes_All.add(new Pair<Integer, MyFilterModes>(itemId, MyFilterModes.Person_Online));

            itemId++;
            _FilterModes_All.add(new Pair<Integer, MyFilterModes>(itemId, MyFilterModes.Group));

            itemId++;
            _FilterModes_All.add(new Pair<Integer, MyFilterModes>(itemId, MyFilterModes.Channel));

        }
        return _FilterModes_All;
    }

    public static void Filter_UI_MenuItem_FillSubs(ActionBarMenuItem menuItem, int idStart) {
        for (int i1 = 0; i1 < FilterModes_All().size(); i1++) {
            menuItem.addSubItem(idStart + FilterModes_All().get(i1).first
                    , ApplicationLoader.applicationContext.getString(Filter_Mode_ResId_String(FilterModes_All().get(i1).second))
                    , Filter_Mode_ResId_Icon(FilterModes_All().get(i1).second, true));
        }
    }

    public static MyFilterModes Filter_UI_GetModeByItemId(int idStart, int itemId) {
        MyFilterModes r = MyFilterModes.All;
        int itemIdInHash = itemId - idStart;
        for (int i1 = 0; i1 < FilterModes_All().size(); i1++) {
            if (itemIdInHash == FilterModes_All().get(i1).first) {
                r = FilterModes_All().get(i1).second;
                break;
            }
        }
        return r;
    }

    private static ArrayList<TLRPC.TL_dialog> _Filter_GetDialogsArray_Cache = null;

    private static void Filter_GetDialogsArray_RefreshCache(MyFilterModes myFilterMode, int dialogsType) {
        ArrayList<TLRPC.TL_dialog> rAll = null;
        if (dialogsType == 0) {
            rAll = MessagesController.getInstance().dialogs;
        } else if (dialogsType == 1) {
            rAll = MessagesController.getInstance().dialogsServerOnly;
        } else if (dialogsType == 2) {
            rAll = MessagesController.getInstance().dialogsGroupsOnly;
        }

        ArrayList<TLRPC.TL_dialog> r = null;
        if (myFilterMode == MyFilterModes.Favourite) {
            if (rAll != null) {
                ArrayList<Integer> myFavourites = MyFavourites.Favourite_GetList(ApplicationLoader.applicationContext, UserConfig.getClientUserId());

                r = new ArrayList<>();
                for (TLRPC.TL_dialog feDialog : rAll) {
                    int targetId = MyConstants.Dialog_GetSenderId(feDialog.id);

                    if (targetId > 0) {
                        if (myFavourites.contains(Integer.valueOf(targetId))) {
                            r.add(feDialog);
                        }
                    }
                }
            }
        } else if (myFilterMode == MyFilterModes.Person) {
            if (rAll != null) {
                r = new ArrayList<>();
                for (TLRPC.TL_dialog feDialog : rAll) {
                    TLObject object = MyConstants.Dialog_GetSender(feDialog.id);
                    if (object instanceof TLRPC.User)
                        r.add(feDialog);
                }
            }
        } else if (myFilterMode == MyFilterModes.Person_Online) {
            if (rAll != null) {
                r = new ArrayList<>();
                for (TLRPC.TL_dialog feDialog : rAll) {
                    TLObject object = MyConstants.Dialog_GetSender(feDialog.id);
                    if (object instanceof TLRPC.User) {
                        TLRPC.User user = (TLRPC.User) object;
                        boolean isOnline = false;
                        if (user == null || user.status == null || user.status.expires == 0 || UserObject.isDeleted(user) || user instanceof TLRPC.TL_userEmpty) {
                            //return getString("ALongTimeAgo", org.telegram.messenger.R.string.ALongTimeAgo);
                            isOnline = false;
                        } else {
                            int currentTime = ConnectionsManager.getInstance().getCurrentTime();
                            if (user.status.expires > currentTime) {
                                //return getString("Online", org.telegram.messenger.R.string.Online);
                                isOnline = true;
                            } else {
                                if (user.status.expires == -1) {
                                    //return getString("Invisible", org.telegram.messenger.R.string.Invisible);
                                    isOnline = false;
                                } else if (user.status.expires == -100) {
                                    //return getString("Lately", org.telegram.messenger.R.string.Lately);
                                    isOnline = false;
                                } else if (user.status.expires == -101) {
                                    //return getString("WithinAWeek", org.telegram.messenger.R.string.WithinAWeek);
                                    isOnline = false;
                                } else if (user.status.expires == -102) {
                                    //return getString("WithinAMonth", org.telegram.messenger.R.string.WithinAMonth);
                                    isOnline = false;
                                } else {
                                    //return formatDateOnline(user.status.expires);
                                    isOnline = false;
                                }
                            }
                        }

                        if (isOnline)
                            r.add(feDialog);
                    }
                }
            }
        } else if (myFilterMode == MyFilterModes.Channel) {
            if (rAll != null) {
                r = new ArrayList<>();
                for (TLRPC.TL_dialog feDialog : rAll) {
                    TLObject object = MyConstants.Dialog_GetSender(feDialog.id);
                    if (object instanceof TLRPC.Chat && ChatObject.isChannel((TLRPC.Chat) object))
                        r.add(feDialog);
                }
            }
        } else if (myFilterMode == MyFilterModes.Group) {
            if (rAll != null) {
                r = new ArrayList<>();
                for (TLRPC.TL_dialog feDialog : rAll) {
                    TLObject object = MyConstants.Dialog_GetSender(feDialog.id);
                    if (object instanceof TLRPC.Chat && !ChatObject.isChannel((TLRPC.Chat) object))
                        r.add(feDialog);
                }
            }
        } else
            r = rAll;

        _Filter_GetDialogsArray_Cache = r;
    }

    private static MyFilterModes _Filter_GetDialogsArray_ArgPrev_1 = MyFilterModes.All;
    private static int _Filter_GetDialogsArray_ArgPrev_2 = -1;

    public static ArrayList<TLRPC.TL_dialog> Filter_GetDialogsArray(MyFilterModes myFilterMode, int dialogsType) {
        if (_Filter_GetDialogsArray_Cache == null || myFilterMode != _Filter_GetDialogsArray_ArgPrev_1 || dialogsType != _Filter_GetDialogsArray_ArgPrev_2)
            Filter_GetDialogsArray_RefreshCache(myFilterMode, dialogsType);
        _Filter_GetDialogsArray_ArgPrev_1 = myFilterMode;
        _Filter_GetDialogsArray_ArgPrev_2 = dialogsType;
        return _Filter_GetDialogsArray_Cache;
    }

    public static void Filter_GetDialogsArray_Reset() {
        _Filter_GetDialogsArray_Cache = null;
    }
}
