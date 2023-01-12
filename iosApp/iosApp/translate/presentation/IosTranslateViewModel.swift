//
//  IosTranslateViewModel.swift
//  iosApp
//
//  Created by Abrar Wiryawan on 12/01/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension TranslateScreen {
    
    @MainActor class IosTranslateViewModel: ObservableObject {
        
        private var historyDataSource: HistoryDataSource
        private var translate: Translate
        
        private let viewModel: TranslateViewModel
        
        @Published var state: TranslateState = TranslateState(
            fromText: "",
            toText: nil,
            fromLanguage: UiLanguage(language: .indonesian, imageName: "indonesian"),
            toLanguage: UiLanguage(language: .english, imageName: "english"),
            isChoosingFromLanguage: false,
            isChoosingToLanguage: false,
            isTranslating: false,
            error: nil,
            history: []
        )
        
        private var handle: IosDisposableHandle?
        
        init(historyDataSource: HistoryDataSource, translate: Translate) {
            self.historyDataSource = historyDataSource
            self.translate = translate
            self.viewModel = TranslateViewModel(
                translate: translate,
                historyDataSource: historyDataSource,
                coroutineScope: nil
            )
        }
        
        func onEvent(event: TranslateEvent) {
            self.viewModel.onEvent(event: event)
        }
        
        func startObserving() {
            handle = viewModel.state.subscribe(onCollect: { nullableState in
                if let state = nullableState {
                    self.state = state
                }
            })
        }
        
        func dispose() {
            handle?.dispose()
        }
    }
}
