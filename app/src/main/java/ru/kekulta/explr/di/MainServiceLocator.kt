package ru.kekulta.explr.di

import android.annotation.SuppressLint
import android.content.Context
import ru.kekulta.explr.features.difflist.data.HashedRepositoryImpl
import ru.kekulta.explr.features.difflist.domain.api.HashedRepository
import ru.kekulta.explr.features.difflist.domain.api.usecases.HashedInteractor
import ru.kekulta.explr.features.difflist.domain.impl.HashedInteractorImpl
import ru.kekulta.explr.features.list.data.FilesRepositoryImpl
import ru.kekulta.explr.features.list.data.database.AppDatabase
import ru.kekulta.explr.features.list.domain.api.usecases.FileUtil
import ru.kekulta.explr.features.list.domain.api.usecases.FilesInteractor
import ru.kekulta.explr.features.list.domain.api.FilesRepository
import ru.kekulta.explr.features.list.domain.api.usecases.FilterManager
import ru.kekulta.explr.features.list.domain.api.usecases.SortingManager
import ru.kekulta.explr.features.list.domain.impl.FileUtilImpl
import ru.kekulta.explr.features.list.domain.impl.FilesInteractorImpl
import ru.kekulta.explr.features.list.domain.impl.FilterManagerImpl
import ru.kekulta.explr.features.list.domain.impl.SortingManagerImpl
import ru.kekulta.explr.features.main.domain.api.ToolBarManager
import ru.kekulta.explr.features.main.domain.impl.ToolBarManagerImpl
import ru.kekulta.explr.shared.navigation.AppRouter
import ru.kekulta.explr.shared.navigation.api.Router

// The object lifetime is the same as application lifetime
@SuppressLint("StaticFieldLeak")
object MainServiceLocator {
    private var _context: Context? = null
    private var database: AppDatabase? = null
    private val context get() = requireNotNull(_context) { "DI should be initialized before use!" }
    private var router: Router? = null
    private var filesRepository: FilesRepository? = null
    private var filesInteractor: FilesInteractor? = null
    private var filterManager: FilterManager? = null
    private var sortingManager: SortingManager? = null
    private var toolBarManager: ToolBarManager? = null
    private var fileUtil: FileUtil? = null
    private var hashedInteractor: HashedInteractor? = null
    private var hashedRepository: HashedRepository? = null

    fun provideFileUtil(): FileUtil {
        if (fileUtil == null) {
            fileUtil = FileUtilImpl(context)
        }
        return fileUtil!!
    }

    fun provideHashedInteractor(): HashedInteractor {
        if (hashedInteractor == null) {
            hashedInteractor = HashedInteractorImpl(
                provideHashedRepository(),
                provideFileUtil(),
                provideFilterManager(),
            )
        }
        return hashedInteractor!!
    }

    fun provideFilesInteractor(): FilesInteractor {
        if (filesInteractor == null) {
            filesInteractor =
                FilesInteractorImpl(
                    provideFilesRepository(),
                    provideFilterManager(),
                    provideSortingManager(),
                    provideFileUtil(),
                )
        }
        return filesInteractor!!
    }

    fun provideToolBarManager(): ToolBarManager {
        if (toolBarManager == null) {
            toolBarManager = ToolBarManagerImpl()
        }
        return toolBarManager!!
    }

    fun provideSortingManager(): SortingManager {
        if (sortingManager == null) {
            sortingManager = SortingManagerImpl()
        }

        return sortingManager!!
    }

    fun provideFilterManager(): FilterManager {
        if (filterManager == null) {
            filterManager = FilterManagerImpl()
        }
        return filterManager!!
    }

    private fun provideHashedRepository(): HashedRepository {
        if (hashedRepository == null) {
            hashedRepository = HashedRepositoryImpl(provideDatabase().getHashedDao())
        }
        return hashedRepository!!
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