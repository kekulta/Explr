package ru.kekulta.explr.di

import android.annotation.SuppressLint
import android.content.Context
import ru.kekulta.explr.features.list.data.FilesRepositoryImpl
import ru.kekulta.explr.features.list.data.database.AppDatabase
import ru.kekulta.explr.features.list.domain.api.FilesInteractor
import ru.kekulta.explr.features.list.domain.api.FilesRepository
import ru.kekulta.explr.features.list.domain.api.SortingManager
import ru.kekulta.explr.features.list.domain.api.VisibilityManager
import ru.kekulta.explr.features.list.domain.impl.FilesInteractorImpl
import ru.kekulta.explr.features.list.domain.impl.SortingManagerImpl
import ru.kekulta.explr.features.list.domain.impl.VisibilityManagerImpl
import ru.kekulta.explr.shared.navigation.AppRouter
import ru.kekulta.explr.shared.navigation.api.Router

// The object lifetime is the same as application lifetime
@SuppressLint("StaticFieldLeak")
object MainServiceLocator {
    private var _context: Context? = null
    private var database: AppDatabase? = null
    private val context get() = requireNotNull(_context) { "DI should initialized before use!" }
    private var router: Router? = null
    private var filesRepository: FilesRepository? = null
    private var filesInteractor: FilesInteractor? = null
    private var visibilityManager: VisibilityManager? = null
    private var sortingManager: SortingManager? = null

    fun provideFilesInteractor(): FilesInteractor {
        if (filesInteractor == null) {
            filesInteractor =
                FilesInteractorImpl(
                    provideFilesRepository(),
                    provideVisibilityManager(),
                    provideSortingManager(),
                )
        }
        return filesInteractor!!
    }

    fun provideSortingManager(): SortingManager {
        if (sortingManager == null) {
            sortingManager = SortingManagerImpl()
        }

        return sortingManager!!
    }

    fun provideVisibilityManager(): VisibilityManager {
        if (visibilityManager == null) {
            visibilityManager = VisibilityManagerImpl()
        }
        return visibilityManager!!
    }

    private fun provideFilesRepository(): FilesRepository {
        if (filesRepository == null) {
            filesRepository = FilesRepositoryImpl(provideDatabase().getFileDao())
        }
        return filesRepository!!
    }

    private fun provideDatabase(): AppDatabase {
        if (database == null) {
            AppDatabase.initDatabase(context)
            database = AppDatabase.getDatabase()
        }
        return database!!
    }

    fun provideRouter(): Router {
        if (router == null) {
            router = AppRouter()
        }
        return router!!
    }

    fun initDi(context: Context) {
        _context = context
    }
}