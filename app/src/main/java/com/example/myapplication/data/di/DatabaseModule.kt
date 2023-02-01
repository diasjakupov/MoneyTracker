package com.example.myapplication.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.ChequeDao
import com.example.myapplication.data.dao.CreditCardDao
import com.example.myapplication.data.db.MyDatabase
import com.example.myapplication.data.repository.Repository
import com.example.myapplication.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesRoomDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(context, MyDatabase::class.java, "money_tracker").build()
    }

    @Provides
    @Singleton
    fun providesCreditCardDao(db: MyDatabase): CreditCardDao {
        return db.creditCardDao()
    }


    @Provides
    @Singleton
    fun providesRepository(
        creditDao: CreditCardDao,
        chequeDao: ChequeDao
    ): RepositoryImpl {
        return RepositoryImpl(creditDao, chequeDao)
    }


    @Provides
    @Singleton
    fun providesChequeDao(db: MyDatabase): ChequeDao {
        return db.chequeDao()
    }
}