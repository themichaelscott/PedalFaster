package com.pedalfaster.launcher.dagger

object Injector {
    private var appComponent: AppComponent? = null

    fun init(appModule: AppModule) {
        appComponent = DaggerAppComponent.builder().appModule(appModule).build()
    }

    /**
     * Retrieves the AppComponent responsible for injection.
     * @return The AppComponent responsible for injection
     * @throws NullPointerException if Injector.init() hasn't been called
     */
    fun get(): AppComponent {
        return appComponent!!
    }
}