//
//  TranslateScreen.swift
//  iosApp
//
//  Created by Abrar Wiryawan on 12/01/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TranslateScreen: View {
    
    private var historyDataSource: HistoryDataSource
    private var translate: Translate
    @ObservedObject var viewModel: IosTranslateViewModel
    
    init(historyDataSource: HistoryDataSource, translate: Translate) {
        self.historyDataSource = historyDataSource
        self.translate = translate
        self.viewModel = IosTranslateViewModel(historyDataSource: self.historyDataSource, translate: self.translate)
    }
    
    var body: some View {
        ZStack {
            List {
                // MARK: Language Picker
                HStack(alignment: .center) {
                    LanguageDropDown(
                        language: viewModel.state.fromLanguage,
                        isOpen: viewModel.state.isChoosingFromLanguage
                    ) { language in
                        viewModel.onEvent(
                            event: TranslateEvent.ChooseFromLanguage(language: language)
                        )
                    }
                    Spacer()
                    SwapLanguageButton {
                        viewModel.onEvent(event: TranslateEvent.SwapLanguage())
                    }
                    Spacer()
                    LanguageDropDown(
                        language: viewModel.state.toLanguage,
                        isOpen: viewModel.state.isChoosingToLanguage
                    ) { language in
                        viewModel.onEvent(
                            event: TranslateEvent.ChooseToLanguage(language: language)
                        )
                    }
                }
                .listRowSeparator(.hidden)
                .listRowBackground(Color.background)
                
                // MARK: TranslateTextField
                TranslateTextField(
                    fromText: Binding(get: {
                        viewModel.state.fromText
                    }, set: { value in
                        viewModel.onEvent(event: TranslateEvent.ChangeTranslationText(text: value))
                    }),
                    fromLanguage: viewModel.state.fromLanguage,
                    toText: viewModel.state.toText,
                    toLanguage: viewModel.state.toLanguage,
                    isTranslating: viewModel.state.isTranslating,
                    onTranslateEvent: { event in
                        viewModel.onEvent(event: event)
                    }
                )
                .listRowSeparator(.hidden)
                .listRowBackground(Color.background)
                
                if !viewModel.state.history.isEmpty {
                    Text("History")
                        .font(.title)
                        .bold()
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.background)
                }
                
                ForEach(viewModel.state.history, id: \.self.id) { item in
                    TranslateHistoryItem(item: item) {
                        viewModel.onEvent(
                            event: TranslateEvent.SelectHistoryItem(item: item)
                        )
                    }
                    .listRowSeparator(.hidden)
                    .listRowBackground(Color.background)
                }
            }
            .listStyle(.plain)
            .buttonStyle(.plain)
            
            VStack {
                Spacer()
                NavigationLink {
                    Text("Voice To Text Screen")
                } label: {
                    ZStack {
                        Circle()
                            .foregroundColor(.primaryColor)
                            .padding()
                        Image(uiImage: UIImage(named: "mic")!)
                            .foregroundColor(.onPrimary)
                    }
                    .frame(maxWidth: 100, maxHeight: 100)
                }

            }
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}

//struct TranslateScreen_Previews: PreviewProvider {
//    static var previews: some View {
//        TranslateScreen()
//    }
//}
