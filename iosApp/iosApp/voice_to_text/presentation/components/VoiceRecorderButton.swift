//
//  VoiceRecorderButton.swift
//  iosApp
//
//  Created by Abrar Wiryawan on 16/01/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct VoiceRecorderButton: View {
    
    var displayState: DisplayState
    var onClick: () -> Void
    
    var body: some View {
        Button(action: onClick) {
            ZStack {
                Circle()
                    .foregroundColor(.primaryColor)
                    .padding()
                icon
                    .foregroundColor(.onPrimary)
            }
        }
        .frame(maxWidth: 96, maxHeight: 96)
    }
    
    var icon: some View {
        switch displayState {
        case.speaking:
            return Image(systemName: "stop.fill")
        case.displayingResult:
            return Image(systemName: "checkmark")
        default:
            return Image(uiImage: UIImage(named: "mic")!)
        }
    }
}

struct VoiceRecorderButton_Previews: PreviewProvider {
    static var previews: some View {
        VoiceRecorderButton(
            displayState: DisplayState.waitingToSpeak
        ) {
            
        }
    }
}
