package com.tonywww.dustandash.block.entity.FissionReactor;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ReactorStructureDiagnostic(@Nullable ReactorStructureSnapshot structure,
                                         ReactorStructureIssue issue,
                                         @Nullable BlockPos problemPos) {
    public static ReactorStructureDiagnostic formed(ReactorStructureSnapshot structure) {
        return new ReactorStructureDiagnostic(structure, ReactorStructureIssue.NONE, null);
    }

    public static ReactorStructureDiagnostic invalid(ReactorStructureIssue issue, BlockPos problemPos) {
        return new ReactorStructureDiagnostic(null, issue, problemPos.immutable());
    }

    public static ReactorStructureDiagnostic invalid(ReactorStructureIssue issue) {
        return new ReactorStructureDiagnostic(null, issue, null);
    }

    public boolean isFormed() {
        return structure != null;
    }

    public Optional<ReactorStructureSnapshot> structureOptional() {
        return Optional.ofNullable(structure);
    }
}