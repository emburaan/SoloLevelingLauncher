package com.sumit.clock.ui.alarm;

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\u001a\b\u0010\u0002\u001a\u00020\u0003H\u0003\u001a&\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bH\u0007\u001a\u001e\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00072\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bH\u0007\u001a\u001e\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bH\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"alarmInputBaseStyle", "Landroidx/compose/ui/text/TextStyle;", "alarmFieldColors", "Landroidx/compose/material3/TextFieldColors;", "MathChallenge", "", "problemCount", "", "difficulty", "Lcom/sumit/clock/alarm/MathDifficulty;", "onComplete", "Lkotlin/Function0;", "ShakeChallenge", "targetCount", "TypingChallenge", "phrase", "", "clock_debug"})
public final class DismissChallengesKt {
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.text.TextStyle alarmInputBaseStyle = null;
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final androidx.compose.material3.TextFieldColors alarmFieldColors() {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void MathChallenge(int problemCount, @org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.MathDifficulty difficulty, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onComplete) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ShakeChallenge(int targetCount, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onComplete) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void TypingChallenge(@org.jetbrains.annotations.NotNull()
    java.lang.String phrase, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onComplete) {
    }
}