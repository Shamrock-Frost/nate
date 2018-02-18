package com.beanie.nate.nate

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.service.quicksettings.TileService

/**
 * Created by chris on 2/17/18.
 */
public class QuickAddNote : TileService() {
    override fun onClick() {
        super.onClick()

        // Called when the user click the tile
        val calendarIntent = Intent(Intent.ACTION_EDIT)
        calendarIntent.type = "vnd.android.cursor.item/event"

        startActivityAndCollapse(calendarIntent)
    }

    override fun onTileRemoved() {
        super.onTileRemoved()

        // Do something when the user removes the Tile
    }

    override fun onTileAdded() {
        super.onTileAdded()

        // Do something when the user add the Tile
    }

    override fun onStartListening() {
        super.onStartListening()

        // Called when the Tile becomes visible
    }

    override fun onStopListening() {
        super.onStopListening()

        // Called when the tile is no longer visible
    }
}