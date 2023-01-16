//
//  IosVoiceToTextViewModel.swift
//  iosApp
//
//  Created by Abrar Wiryawan on 16/01/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Combine
import shared

@MainActor
class IosVoiceToTextViewModel: ObservableObject {
    private var parser: any VoiceToTextParser
    private var languageCode: String
    private let viewModel: VoiceToTextViewModel
    
    @Published var state = VoiceToTextState(
        powerRatios: [],
        spokenText: "",
        canRecord: false,
        recordError: nil,
        displayState: nil
    )
    private var handle: IosDisposableHandle?
    
    init(parser: VoiceToTextParser, languageCode: String) {
        self.parser = parser
        self.languageCode = languageCode
        self.viewModel = VoiceToTextViewModel(parser: self.parser, coroutineScope: nil)
    }
    
    func onEvent(event: VoiceToTextEvent) {
        viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.subscribe { [weak self] nullableState in
            if let state = nullableState {
                self?.state = state
            }
        }
    }
    
    func dispose() {
        handle?.dispose()
        onEvent(event: VoiceToTextEvent.Reset())
    }
    
}
