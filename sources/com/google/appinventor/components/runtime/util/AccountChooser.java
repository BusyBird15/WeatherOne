package com.google.appinventor.components.runtime.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.SynchronousQueue;

public class AccountChooser {
    private static final String ACCOUNT_PREFERENCE = "account";
    private static final String ACCOUNT_TYPE = "com.google";
    private static final String LOG_TAG = "AccountChooser";
    private static final String NO_ACCOUNT = "";
    private AccountManager accountManager;
    /* access modifiers changed from: private */
    public Activity activity;
    /* access modifiers changed from: private */
    public String chooseAccountPrompt;
    private String preferencesKey;
    private String service;

    public AccountChooser(Activity activity2, String service2, String title, String key) {
        this.activity = activity2;
        this.service = service2;
        this.chooseAccountPrompt = title;
        this.preferencesKey = key;
        this.accountManager = AccountManager.get(activity2);
    }

    public Account findAccount() {
        Account account;
        Account[] accounts = this.accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length == 1) {
            persistAccountName(accounts[0].name);
            return accounts[0];
        } else if (accounts.length == 0) {
            String accountName = createAccount();
            if (accountName != null) {
                persistAccountName(accountName);
                return this.accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
            }
            Log.i(LOG_TAG, "User failed to create a valid account");
            return null;
        } else {
            String accountName2 = getPersistedAccountName();
            if (accountName2 != null && (account = chooseAccount(accountName2, accounts)) != null) {
                return account;
            }
            String accountName3 = selectAccount(accounts);
            if (accountName3 != null) {
                persistAccountName(accountName3);
                return chooseAccount(accountName3, accounts);
            }
            Log.i(LOG_TAG, "User failed to choose an account");
            return null;
        }
    }

    private Account chooseAccount(String accountName, Account[] accounts) {
        for (Account account : accounts) {
            if (account.name.equals(accountName)) {
                Log.i(LOG_TAG, "chose account: " + accountName);
                return account;
            }
        }
        return null;
    }

    private String createAccount() {
        Log.i(LOG_TAG, "Adding auth token account ...");
        try {
            String accountName = this.accountManager.addAccount(ACCOUNT_TYPE, this.service, (String[]) null, (Bundle) null, this.activity, (AccountManagerCallback) null, (Handler) null).getResult().getString("authAccount");
            Log.i(LOG_TAG, "created: " + accountName);
            return accountName;
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String selectAccount(android.accounts.Account[] r9) {
        /*
            r8 = this;
            java.util.concurrent.SynchronousQueue r3 = new java.util.concurrent.SynchronousQueue
            r3.<init>()
            com.google.appinventor.components.runtime.util.AccountChooser$SelectAccount r4 = new com.google.appinventor.components.runtime.util.AccountChooser$SelectAccount
            r4.<init>(r9, r3)
            r4.start()
            java.lang.String r5 = "AccountChooser"
            java.lang.String r6 = "Select: waiting for user..."
            android.util.Log.i(r5, r6)
            r1 = 0
            java.lang.Object r5 = r3.take()     // Catch:{ InterruptedException -> 0x003f }
            r0 = r5
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ InterruptedException -> 0x003f }
            r1 = r0
        L_0x001d:
            java.lang.String r5 = "AccountChooser"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Selected: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r1)
            java.lang.String r6 = r6.toString()
            android.util.Log.i(r5, r6)
            java.lang.String r5 = ""
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x003e
            r1 = 0
        L_0x003e:
            return r1
        L_0x003f:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x001d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.AccountChooser.selectAccount(android.accounts.Account[]):java.lang.String");
    }

    private SharedPreferences getPreferences() {
        return this.activity.getSharedPreferences(this.preferencesKey, 0);
    }

    private String getPersistedAccountName() {
        return getPreferences().getString(ACCOUNT_PREFERENCE, (String) null);
    }

    private void persistAccountName(String accountName) {
        Log.i(LOG_TAG, "persisting account: " + accountName);
        getPreferences().edit().putString(ACCOUNT_PREFERENCE, accountName).commit();
    }

    public void forgetAccountName() {
        getPreferences().edit().remove(ACCOUNT_PREFERENCE).commit();
    }

    class SelectAccount extends Thread implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
        /* access modifiers changed from: private */
        public String[] accountNames;
        private SynchronousQueue<String> queue;

        SelectAccount(Account[] accounts, SynchronousQueue<String> queue2) {
            this.queue = queue2;
            this.accountNames = new String[accounts.length];
            for (int i = 0; i < accounts.length; i++) {
                this.accountNames[i] = accounts[i].name;
            }
        }

        public void run() {
            AccountChooser.this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(AccountChooser.this.activity).setTitle(Html.fromHtml(AccountChooser.this.chooseAccountPrompt)).setOnCancelListener(SelectAccount.this).setSingleChoiceItems(SelectAccount.this.accountNames, -1, SelectAccount.this).show();
                    Log.i(AccountChooser.LOG_TAG, "Dialog showing!");
                }
            });
        }

        public void onClick(DialogInterface dialog, int button) {
            if (button >= 0) {
                try {
                    String account = this.accountNames[button];
                    Log.i(AccountChooser.LOG_TAG, "Chose: " + account);
                    this.queue.put(account);
                } catch (InterruptedException e) {
                }
            } else {
                this.queue.put("");
            }
            dialog.dismiss();
        }

        public void onCancel(DialogInterface dialog) {
            Log.i(AccountChooser.LOG_TAG, "Chose: canceled");
            onClick(dialog, -1);
        }
    }
}
