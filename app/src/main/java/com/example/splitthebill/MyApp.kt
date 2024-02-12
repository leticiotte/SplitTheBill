package com.example.splitthebill

import android.app.Application
import com.example.splitthebill.domain.repository.MemberRepository
import com.example.splitthebill.infrastructure.repository.MemberRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}

val appModule = module {
    single<MemberRepository> { MemberRepositoryImpl() }
}

