//
//  VoiceRecorderDisplay.swift
//  iosApp
//
//  Created by Abrar Wiryawan on 16/01/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct VoiceRecorderDisplay: View {
    
    var powerRatios: [Double]
    
    var body: some View {
        Canvas { graphicsContext, size in
            graphicsContext.clip(
                to: Path(
                    CGRect(origin: .zero, size: size)
                )
            )
            
            let barWidth = 3.0
            let barCount = Int(size.width / Double(2 * barWidth))
            let defaultLevel = 0.05
            let reversedRatios = powerRatios
                .map { ratio in
                    min(max(defaultLevel, ratio), 1.0)
                }
                .suffix(barCount)
                .reversed()
            
            for (i, powerRatio) in reversedRatios.enumerated() {
                let centerY = CGFloat(size.height / 2.0)
                let yTopStart = CGFloat(centerY - centerY * powerRatio)
                
                var path = Path()
                path.addRoundedRect(
                    in: CGRect(
                        x: CGFloat(size.width) - CGFloat(i) * 2.0 * barWidth,
                        y: yTopStart,
                        width: barWidth,
                        height: (centerY - yTopStart) * 2.0
                    ),
                    cornerSize: CGSize(width: 10, height: 10)
                )
                graphicsContext.fill(path, with: .color(.primaryColor))
            }
        }
        .gradientSurface()
        .cornerRadius(24)
        .padding(.horizontal, 16)
        .padding(.vertical, 8)
        .shadow(radius: 4)
    }
}

struct VoiceRecorderDisplay_Previews: PreviewProvider {
    static var previews: some View {
        VoiceRecorderDisplay(
            powerRatios: [5, 15, 20, 40, 25, 30, 40, 50, 10, 60, 10, 20, 30, 50]
        )
    }
}
