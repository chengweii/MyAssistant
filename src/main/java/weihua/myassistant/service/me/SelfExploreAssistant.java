package weihua.myassistant.service.me;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import weihua.myassistant.context.Context;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.service.Assistant;
import weihua.myassistant.service.me.entity.BodyState;
import weihua.myassistant.service.me.entity.BodyStateType;
import weihua.myassistant.service.me.entity.MindState;
import weihua.myassistant.service.me.entity.MindStateType;
import weihua.myassistant.service.me.entity.OuterSign;
import weihua.myassistant.service.me.entity.OuterSignType;
import weihua.myassistant.service.me.entity.Reaction;
import weihua.myassistant.service.me.entity.SelfExploreRequest;
import weihua.myassistant.util.GsonUtil;

/**
 * 帮助、咨询助手服务
 * 
 * @author chengwei2
 * 
 */
public class SelfExploreAssistant implements Assistant {

	private Me me;

	@Override
	public String getResponse(String request, RequestType requestType, Context context) {
		SelfExploreRequest selfExploreRequest = null;
		Type type = new TypeToken<SelfExploreRequest>() {
		}.getType();
		try {
			selfExploreRequest = GsonUtil.gson.fromJson(request, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		me = new Me(selfExploreRequest.getBodyState(), selfExploreRequest.getMindState());
		Reaction reaction = me.getResponse(selfExploreRequest.getOuterSign());
		return reaction.toString();
	}

	public static void main(String[] args) {
		String response = new SelfExploreAssistant().getResponse(
				"{  \"bodyState\": {    \"bodyStateType\": \"1\",    \"content\": \"\"  },  \"mindState\": {    \"mindStateType\": \"1\",    \"content\": \"\"  },  \"outerSign\": {    \"outerSignType\": \"2\",    \"content\": \"\"  }}",
				RequestType.TEXT, null);
		/*
		 * SelfExploreRequest selfExploreRequest = new SelfExploreRequest();
		 * selfExploreRequest.bodyState = new BodyState();
		 * selfExploreRequest.bodyState.bodyStateType = BodyStateType.FINE;
		 * selfExploreRequest.bodyState.content = "";
		 * selfExploreRequest.mindState = new MindState();
		 * selfExploreRequest.mindState.mindStateType = MindStateType.FINE;
		 * selfExploreRequest.mindState.content = "";
		 * selfExploreRequest.outerSign = new OuterSign();
		 * selfExploreRequest.outerSign.outerSignType = OuterSignType.HEAR;
		 * selfExploreRequest.outerSign.content = "";
		 * System.out.print(GsonUtil.gson.toJson(selfExploreRequest));
		 */
		System.out.print(response);
	}

}
