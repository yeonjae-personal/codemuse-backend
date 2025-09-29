package com.lgcns.svcp.prod.ui.prod.dto.extend;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TargetResDto {
	private List<OffrGrpResDto> leaderGrp;
	private List<OffrGrpResDto> followerGrp;
}
