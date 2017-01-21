package weihua.myassistant.service.me.entity;

public class SelfExploreRequest {
	public RequestBodyState bodyState;
	public RequestMindState mindState;
	public RequestOuterSign outerSign;

	public BodyState getBodyState() {
		BodyState entity = new BodyState();
		entity.bodyStateType = BodyStateType.fromCode(bodyState.bodyStateType);
		entity.content = bodyState.content;
		return entity;
	}

	public MindState getMindState() {
		MindState entity = new MindState();
		entity.mindStateType = MindStateType.fromCode(mindState.mindStateType);
		entity.content = mindState.content;
		return entity;
	}

	public OuterSign getOuterSign() {
		OuterSign entity = new OuterSign();
		entity.outerSignType = OuterSignType.fromCode(outerSign.outerSignType);
		entity.content = outerSign.content;
		return entity;
	}

	public static class RequestBodyState {
		public String bodyStateType;
		public String content;
	}

	public class RequestMindState {
		public String mindStateType;
		public String content;
	}

	public class RequestOuterSign {
		public String outerSignType;
		public String content;
	}
}
