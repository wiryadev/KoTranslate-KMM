//
//  MicPowerObserver.swift
//  iosApp
//
//  Created by Abrar Wiryawan on 16/01/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Speech
import Combine
import shared

class MicPowerObserver: ObservableObject {
    
    private var cancellable: AnyCancellable? = nil
    private var audioRecorder: AVAudioRecorder? = nil
    
    @Published private(set) var micPowerRatio = 0.0
    
    private let powerRatioEmissionsPersecond = 20.0
    
    func startObserving() {
        do {
            let recorderSettings: [String: Any] = [
                AVFormatIDKey: NSNumber(value: kAudioFormatAppleLossless),
                AVNumberOfChannelsKey: 1
            ]
            
            let recorder = try AVAudioRecorder(
                url: URL(fileURLWithPath: "/dev/null", isDirectory: true),
                settings: recorderSettings
            )
            recorder.isMeteringEnabled = true
            recorder.record()
            self.audioRecorder = recorder
            
            self.cancellable = Timer.publish(
                every: 1.0 / powerRatioEmissionsPersecond,
                tolerance: 1.0 / powerRatioEmissionsPersecond,
                on: .main,
                in: .common
            )
            .autoconnect()
            .sink { [weak self] _ in
                recorder.updateMeters()
                
                let powerOffset = recorder.averagePower(forChannel: 0)
                if powerOffset < -50 {
                    self?.micPowerRatio = 0.0
                } else {
                    let normalizeOffset = CGFloat(50 + powerOffset) / 50
                    self?.micPowerRatio = normalizeOffset
                }
            }
            
        } catch {
            print("An error occured when observing microphone power: \(error.localizedDescription)")
        }
    }
    
    func relase() {
        cancellable = nil
        
        audioRecorder?.stop()
        audioRecorder = nil
        
        micPowerRatio = 0.0
    }
    
}
