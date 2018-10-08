package com.example.danilius.phoneapp.data;

import android.provider.BaseColumns;

public final class PhoneContract {
    private PhoneContract() {    };

    public static final class PhoneEntry implements BaseColumns {
        public final static String TABLE_NAME = "phonebook";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_PHONENUMBER = "phonenumber";
        public final static String COLUMN_EMAIL = "email";

    }
}
