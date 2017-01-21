package weihua.myassistant.service.me.entity;

import java.util.ArrayList;
import java.util.List;

public class Reaction {
	public List<ReactionResult> reactionResultList = new ArrayList<ReactionResult>();

	public void addReaction(String result, Reactor reactor) {
		ReactionResult reactionResult = new ReactionResult();
		reactionResult.result = result;
		reactionResult.reactor = reactor;
		reactionResultList.add(reactionResult);
	}

	public static class ReactionResult {
		public String result;
		public Reactor reactor;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ReactionResult reactionResult : reactionResultList) {
			sb.append(reactionResult.reactor.getValue());
			sb.append(":");
			sb.append(reactionResult.result);
		}

		return sb.toString();
	}
}
