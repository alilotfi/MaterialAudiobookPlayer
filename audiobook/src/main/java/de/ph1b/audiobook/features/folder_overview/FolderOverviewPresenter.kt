package de.ph1b.audiobook.features.folder_overview

import de.ph1b.audiobook.injection.App
import de.ph1b.audiobook.misc.value
import de.ph1b.audiobook.mvp.Presenter
import de.ph1b.audiobook.persistence.PrefsManager
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

/**
 * The presenter for [FolderOverviewActivity]
 *
 * @author Paul Woitaschek
 */
class FolderOverviewPresenter : Presenter<FolderOverviewActivity>() {

    init {
        App.component().inject(this)
    }

    @Inject lateinit var prefsManager: PrefsManager

    override fun onBind(view: FolderOverviewActivity, subscriptions: CompositeSubscription) {
        updateFoldersInView()
    }

    private fun updateFoldersInView() {
        val collectionFolders = prefsManager.collectionFolders
        val singleFolders = prefsManager.singleBookFolders
        view!!.updateAdapterData(collectionFolders.value(), singleFolders.value())
    }

    /**
     * Removes a folder that is either a collection book or a single book.
     *
     * @param folder The folder to remove.
     */
    fun removeFolder(folder: String) {
        val collectionFolders = HashSet(prefsManager.collectionFolders.value())
        val singleFolders = HashSet(prefsManager.singleBookFolders.value())

        val colRemoved = collectionFolders.remove(folder)
        if (colRemoved) {
            prefsManager.collectionFolders.set(collectionFolders)
        }

        val singleRemoved = singleFolders.remove(folder)
        if (singleRemoved) {
            prefsManager.singleBookFolders.set(singleFolders)
        }

        updateFoldersInView()
    }
}