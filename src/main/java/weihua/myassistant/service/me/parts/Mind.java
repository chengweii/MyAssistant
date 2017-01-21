package weihua.myassistant.service.me.parts;

import weihua.myassistant.service.me.entity.MindState;
import weihua.myassistant.service.me.entity.OuterSign;
import weihua.myassistant.service.me.entity.Reaction;
import weihua.myassistant.service.me.entity.Reactor;

public class Mind {
	private Subconscious subconscious;
	private Values values;
	private Body body;
	private MindState mindState;

	public Mind(Body body, MindState mindState) {
		this.body = body;
		this.mindState = mindState;
		this.subconscious = new Subconscious(body.bodyState, this.mindState);
		this.values = new Values(body.bodyState, this.mindState);
	}

	public void Handle(OuterSign outerSign, Reaction reaction) {
		subconscious.Handle(outerSign, reaction);
		values.Handle(outerSign, reaction);
		mindHandle(outerSign, reaction);
		body.Handle(reaction);
	}

	private void mindHandle(OuterSign outerSign, Reaction reaction) {
		reaction.addReaction("I dont know", Reactor.fromCode(this.getClass().getName()));
	}
}
