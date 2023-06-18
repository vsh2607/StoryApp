package com.example.mystoryapp.view.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.widget.RemoteViews
import com.example.mystoryapp.R
import com.squareup.picasso.Picasso


class ImageBannerWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_UPDATE_WIDGET = "com.example.appwidget.ACTION_UPDATE_WIDGET"
        const val EXTRA_IMAGE_URL = "com.example.appwidget.EXTRA_IMAGE_URL"
        const val EXTRA_DESCRIPTION = "com.example.appwidget.EXTRA_DESCRIPTION"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val imageUrl = getImageUrlFromPreferences(context)
            val description = getDescriptionFromPreferences(context)
            updateAppWidget(context, appWidgetManager, appWidgetId, imageUrl, description)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_UPDATE_WIDGET) {
            val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
            val description = intent.getStringExtra(EXTRA_DESCRIPTION)

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, ImageBannerWidget::class.java))

            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, imageUrl, description)
            }
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }

    private fun getImageUrlFromPreferences(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(EXTRA_IMAGE_URL, null)
    }

    private fun getDescriptionFromPreferences(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(EXTRA_DESCRIPTION, null)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    imageUrl: String?,
    description: String?
) {
    val views = RemoteViews(context.packageName, R.layout.image_banner_widget)

    if (imageUrl != null && description != null) {
        val bitmap = Picasso.get().load(imageUrl).get()
        views.setImageViewBitmap(R.id.imageView, bitmap)
        views.setTextViewText(R.id.descriptionTextView, description)
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
