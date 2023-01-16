//
//  VoiceToTextScreen.swift
//  iosApp
//
//  Created by Abrar Wiryawan on 16/01/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct VoiceToTextScreen: View {
    
    private let onResult: (String) -> Void
    private let parser: any VoiceToTextParser
    private let languageCode: String
    
    @ObservedObject var viewModel: IosVoiceToTextViewModel
    
    @Environment(\.presentationMode) var presentation
    
    init(
        onResult: @escaping (String) -> Void,
        parser: any VoiceToTextParser,
        languageCode: String
    ) {
        self.onResult = onResult
        self.parser = parser
        self.languageCode = languageCode
        self.viewModel = IosVoiceToTextViewModel(parser: parser, languageCode: languageCode)
    }
    
    var body: some View {
        VStack {
            Spacer()
            
            mainView
            
            Spacer()
            
            HStack {
                Spacer()
                VoiceRecorderButton(
                    displayState: viewModel.state.displayState ?? .waitingToSpeak
                ) {
                    if viewModel.state.displayState != .displayingResult {
                        viewModel.onEvent(event: VoiceToTextEvent.ToggleRecording(languageCode: languageCode))
                    } else {
                        onResult(viewModel.state.spokenText)
                        self.presentation.wrappedValue.dismiss()
                    }
                }
                if viewModel.state.displayState == .displayingResult {
                    Button {
                        viewModel.onEvent(event: VoiceToTextEvent.ToggleRecording(languageCode: languageCode))
                    } label: {
                        Image(systemName: "arrow.clockwise")
                            .foregroundColor(.lightBlue)
                    }

                }
                Spacer()
            }
        }
        .background(Color.background)
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
    
    var mainView: some View {
        if let displayState = viewModel.state.displayState {
            switch displayState {
            case .waitingToSpeak:
                return AnyView(
                    Text("Click record and start talking")
                        .font(.title2)
                )
            case.displayingResult:
                return AnyView(
                    Text(viewModel.state.spokenText)
                        .font(.title2)
                )
            case .error:
                return AnyView(
                    Text(viewModel.state.recordError ?? "Unknown Error")
                        .font(.title2)
                        .foregroundColor(.red)
                )
            case .speaking:
                return AnyView(
                    VoiceRecorderDisplay(
                        powerRatios: viewModel.state.powerRatios
                            .map { Double(truncating: $0) }
                    )
                    .frame(maxHeight: 96)
                    .padding()
                )
            default:
                return AnyView(EmptyView())
            }
        } else {
            return AnyView(EmptyView())
        }
    }
}

struct VoiceToTextScreen_Previews: PreviewProvider {
    static var previews: some View {
        VoiceToTextScreen(
            onResult: {_ in},
            parser: IosVoiceToTextParser(),
            languageCode: "en-US"
        )
    }
}
