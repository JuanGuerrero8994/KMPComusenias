import SwiftUI
import FirebaseCore
import ComposeApp
import Firebase

@main
struct iOSApp: App {
    init() {
        InitKoinKt.doInitKoinForIos()
        FirebaseApp.configure()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
