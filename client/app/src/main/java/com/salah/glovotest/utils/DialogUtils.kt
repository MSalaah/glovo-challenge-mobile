package com.salah.glovotest.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.salah.glovotest.R

/**
 * Created by salah on 12/15/18.
 */
object DialogUtils {

    fun showDefaultDialog(context: Context, title: String, message: String, yesListener: DialogInterface.OnClickListener?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(context.getString(R.string.ok), { dialogInterface, i ->
            dialogInterface.dismiss()
            yesListener?.onClick(dialogInterface, i)
        })
        builder.setNegativeButton(context.getString(R.string.cancel), { dialogInterface, i ->
            dialogInterface.dismiss()
        })

        val alert = builder.create()
        alert.setCanceledOnTouchOutside(false)

        alert.show()
    }
}