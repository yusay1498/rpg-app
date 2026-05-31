package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.domain.entity.CharacterJob;

public record CharacterJobResponse(
        String jobId,
        String jobName,
        boolean mastered,
        int maxLevel) {

    public static CharacterJobResponse from(CharacterJob characterJob) {
        return new CharacterJobResponse(
                characterJob.getId().getJobId(),
                characterJob.getJob().getName(),
                characterJob.isMastered(),
                characterJob.getMaxLevel()
        );
    }
}
