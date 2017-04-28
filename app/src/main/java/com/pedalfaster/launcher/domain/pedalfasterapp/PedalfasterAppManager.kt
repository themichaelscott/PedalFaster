package com.pedalfaster.launcher.domain.pedalfasterapp

import com.pedalfaster.launcher.domain.DatabaseManager
import javax.inject.Inject

@javax.inject.Singleton
class PedalfasterAppManager @Inject constructor(databaseManager: DatabaseManager) : PedalfasterAppBaseManager(databaseManager) {

}