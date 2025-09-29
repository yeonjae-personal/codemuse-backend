from typing import Any, Dict

from ..models import LogicFlow


def build_logic_flow_from_condition_tree(condition_tree: Dict[str, Any]) -> LogicFlow:
    """
    condition_tree(dict)에서 LogicFlow 객체를 생성합니다.

    Why: 룰의 논리 구조를 분석하여 복잡성과 잠재적 문제점을 파악하고 최적화 방향을 제시하기 위함입니다.
    How: 조건 트리를 재귀적으로 순회하여 논리 연산자 사용 빈도, 중첩 레벨 분포, 분기 커버리지를 계산합니다.
    """
    # 예시: AND/OR 카운트, 레벨별 조건 수, 분기 커버리지, 도달 불가능 조건
    logical_operators = {"AND": 0, "OR": 0}
    nesting_levels = []
    branch_coverage = {}
    potential_dead_code = []

    def traverse(node, depth=0):
        if not node:
            return
        if len(nesting_levels) <= depth:
            nesting_levels.append(0)
        nesting_levels[depth] += 1
        node_type = node.get("type")
        if node_type in ("AND", "OR"):
            logical_operators[node_type] += 1
            for child in node.get("children", []):
                traverse(child, depth + 1)
        # 분기 커버리지/도달 불가능 조건 등은 실제 분석 로직에 맞게 구현 필요

    traverse(condition_tree)
    return LogicFlow(
        logical_operators=logical_operators,
        nesting_levels=nesting_levels,
        branch_coverage=branch_coverage,
        potential_dead_code=potential_dead_code,
    )
