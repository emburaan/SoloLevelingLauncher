package com.sumit.clock.ui.alarm;

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u0006\u001aB\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00072\u000e\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0005H\u0007\u001a@\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a,\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\r2\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a$\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u000b2\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a$\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00192\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a>\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u000b2\b\b\u0002\u0010\u001e\u001a\u00020\u000b2\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u00a8\u0006\u001f"}, d2 = {"AlarmEditDialog", "", "initial", "Lcom/sumit/clock/alarm/Alarm;", "onDismiss", "Lkotlin/Function0;", "onSave", "Lkotlin/Function1;", "onDelete", "MathConfig", "problems", "", "difficulty", "Lcom/sumit/clock/alarm/MathDifficulty;", "onProblemsChange", "onDifficultyChange", "DifficultyChip", "value", "selected", "onChange", "ShakeConfig", "count", "onCountChange", "TypingConfig", "phrase", "", "onPhraseChange", "StepperControl", "min", "max", "step", "clock_debug"})
public final class AlarmEditDialogKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AlarmEditDialog(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.Alarm initial, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sumit.clock.alarm.Alarm, kotlin.Unit> onSave, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MathConfig(int problems, com.sumit.clock.alarm.MathDifficulty difficulty, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onProblemsChange, kotlin.jvm.functions.Function1<? super com.sumit.clock.alarm.MathDifficulty, kotlin.Unit> onDifficultyChange) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void DifficultyChip(com.sumit.clock.alarm.MathDifficulty value, com.sumit.clock.alarm.MathDifficulty selected, kotlin.jvm.functions.Function1<? super com.sumit.clock.alarm.MathDifficulty, kotlin.Unit> onChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ShakeConfig(int count, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onCountChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TypingConfig(java.lang.String phrase, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPhraseChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StepperControl(int value, int min, int max, int step, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onChange) {
    }
}