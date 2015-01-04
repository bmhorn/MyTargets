package de.dreier.mytargets.utils;

import android.app.backup.FileBackupHelper;
import android.content.Context;

class DbBackupHelper extends FileBackupHelper {
    public DbBackupHelper(Context ctx, String dbName) {
        super(ctx, ctx.getDatabasePath(dbName).getAbsolutePath());
    }
}