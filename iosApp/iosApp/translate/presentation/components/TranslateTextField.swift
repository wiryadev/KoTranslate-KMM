//
//  TranslateTextField.swift
//  iosApp
//
//  Created by Abrar Wiryawan on 13/01/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import UniformTypeIdentifiers

struct TranslateTextField: View {
    
    @Binding var fromText: String
    let fromLanguage: UiLanguage
    let toText: String?
    let toLanguage: UiLanguage
    let isTranslating: Bool
    let onTranslateEvent: (TranslateEvent) -> Void
    
    var body: some View {
        if toText == nil || isTranslating {
            IdleTextField(
                fromText: $fromText,
                isTranslating: isTranslating,
                onTranslateEvent: onTranslateEvent
            )
            .gradientSurface()
            .cornerRadius(16)
            .animation(.easeInOut, value: isTranslating)
            .shadow(radius: 4)
        } else {
            TranslatedTextField(
                fromText: fromText,
                fromLanguage: fromLanguage,
                toText: toText ?? "",
                toLanguage: toLanguage,
                onTranslateEvent: onTranslateEvent
            )
            .padding()
            .gradientSurface()
            .cornerRadius(16)
            .animation(.easeInOut, value: isTranslating)
            .shadow(radius: 4)
            .onTapGesture {
                onTranslateEvent(TranslateEvent.EditTranslation())
            }
        }
    }
}

struct TranslateTextField_Previews: PreviewProvider {
    static var previews: some View {
        TranslateTextField(
            fromText: Binding(
                get: { "test" },
                set: { _ in }
            ),
            fromLanguage: UiLanguage(
                language: .indonesian,
                imageName: "indonesian"
            ),
            toText: "nil",
            toLanguage: UiLanguage(
                language: .english,
                imageName: "english"
            ),
            isTranslating: false,
            onTranslateEvent: { _ in            }
        )
    }
}

private extension TranslateTextField {
    
    // MARK: IdleTextField
    struct IdleTextField: View {
        @Binding var fromText: String
        let isTranslating: Bool
        let onTranslateEvent: (TranslateEvent) -> Void
        
        var body: some View {
            TextEditor(text: $fromText)
                .frame(
                    maxWidth: .infinity,
                    minHeight: 200,
                    alignment: .topLeading
                )
                .padding()
                .foregroundColor(.onSurface)
                .overlay(alignment: .bottomTrailing) {
                    ProgressButton(
                        text: "Translate",
                        isLoading: isTranslating) {
                            onTranslateEvent(TranslateEvent.Translate())
                        }
                        .padding(.trailing)
                        .padding(.bottom)
                }
                .onAppear {
                    UITextView.appearance().backgroundColor = .clear
                }
        }
    }
    
    // MARK: TranslatedTextField
    struct TranslatedTextField: View {
        let fromText: String
        let fromLanguage: UiLanguage
        let toText: String
        let toLanguage: UiLanguage
        let onTranslateEvent: (TranslateEvent) -> Void
        
        private let tts = TextToSpeech()
        
        var body: some View {
            VStack(alignment: .leading) {
                LanguageDisplay(language: fromLanguage)
                Text(fromText)
                    .foregroundColor(.onSurface)
                HStack {
                    Spacer()
                    Button {
                        UIPasteboard.general.setValue(
                            fromText,
                            forPasteboardType: UTType.plainText.identifier)
                    } label: {
                        Image(uiImage: UIImage(named: "copy")!)
                            .renderingMode(.template)
                            .foregroundColor(.lightBlue)
                    }
                    Button {
                        onTranslateEvent(TranslateEvent.CloseTranslation())
                    } label: {
                        Image(systemName: "xmark")
                            .foregroundColor(.lightBlue)
                    }
                }
                Divider()
                    .padding()
                LanguageDisplay(language: toLanguage)
                    .padding(.bottom)
                Text(toText)
                    .foregroundColor(.onSurface)
                HStack {
                    Spacer()
                    Button {
                        UIPasteboard.general.setValue(
                            toText,
                            forPasteboardType: UTType.plainText.identifier)
                    } label: {
                        Image(uiImage: UIImage(named: "copy")!)
                            .renderingMode(.template)
                            .foregroundColor(.lightBlue)
                    }
                    Button {
                        tts.speak(
                            text: toText,
                            language: toLanguage.language.langCode
                        )
                    } label: {
                        Image(systemName: "speaker.wave.2")
                            .foregroundColor(.lightBlue)
                    }
                }
            }
        }
    }
    
}
