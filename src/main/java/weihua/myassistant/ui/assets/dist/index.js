function initTestInterface() {
	if (!window.JavascriptInterface) {
		window.JavascriptInterface = {
			backHome : function() {
				return "backHome";
			},
			loadDataFile : function(topicFileName) {
				return "loadDataFile:" + topicFileName;
			},
			showMsg : function(msg) {
				alert(msg);
			},
			getResponse : function(request, requestType) {
				return "getResponse";
			}
		};
		assistant.refs.imagePath = "dist/images/";
	}
}

function init() {
	window.assistant = {
		refs : {
			imagePath : "file:///android_asset/dist/images/",
			assistantUserName : "assist",
			meUserName : "me",
			containerId : "m-message",
			contentInputId : "m-textarea",
			btnSendId : "btn_send",
			btnBackId : "btn_back",
			btnLoadId : "btn_load",
			choiceItemClass : "choice-item",
			choiceItemValueName : "choiceValue",
			mediaLinkClass : "media-link",
			mediaLinkValueName : "mediaLink",
			mediaTypeValueName : "mediaType",
			msgTemplateId : "copy-li",
			msgBodyClass : "m-body",
			msgHeaderClass : "m-header",
			msgTimeClass : "m-time",
			msgContentClass : "m-content"
		},
		requestType : {
			text : 0,
			choice : 1
		}
	};

	initTestInterface();

	function initAssistantHeader() {
		var headers = [ "alisa.png", "alice.png", "jenny.png" ];
		var index = Math.floor((Math.random() * headers.length));
		assistant.header = headers[index];
	}
	initAssistantHeader();

	assistant.scrollBottom = function() {
		var m_message = $("#" + assistant.refs.containerId);
		m_message.stop().animate({
			scrollTop : m_message[0].scrollHeight
		}, 1000);
	}

	assistant.backHome = function() {
		$("#" + assistant.refs.containerId + " ul").html("");
		var data = JavascriptInterface.backHome();
		assistant.showMsg(assistant.refs.assistantUserName, data);
	}

	assistant.loadDataFile = function() {
		var topicFileName = prompt(
				"Please input topic file name.(Mind:There is an option must to have a reply,Or this option is the end point.)",
				"");
		if (topicFileName == null) {
			return;
		}
		if (topicFileName) {
			var result = JavascriptInterface.loadDataFile(topicFileName);
			JavascriptInterface.showMsg(result);
		} else {
			JavascriptInterface.showMsg("Please input topic file name!");
		}
		$("#" + assistant.refs.btnLoadId).unbind().click(assistant.loadDataFile);
	}

	assistant.bindChoiceEvent = function(msg_li) {
		msg_li.find("." + assistant.refs.choiceItemClass).click(
				function() {
					var request = $(this).attr(
							assistant.refs.choiceItemValueName);
					var data = JavascriptInterface.getResponse(request,
							assistant.requestType.choice);
					assistant.showMsg(assistant.refs.assistantUserName, data);
				});
	}

	assistant.bindMediaEvent = function(msg_li) {
		msg_li.find("." + assistant.refs.mediaLinkClass).click(function() {
			var mediaLink = $(this).attr(assistant.refs.mediaLinkValueName);
			var mediaType = $(this).attr(assistant.refs.mediaTypeValueName);
			JavascriptInterface.showMedia(mediaLink, mediaType);
		});
	}

	assistant.showMsg = function(user, msg) {
		var class_value = "self";
		var img_value = assistant.refs.imagePath + "me.jpg";
		if (user == assistant.refs.assistantUserName) {
			class_value = "";
			img_value = assistant.refs.imagePath + assistant.header;
		}
		var msg_li = $("#" + assistant.refs.msgTemplateId).clone();
		msg_li.removeAttr("id");
		var date = new Date();
		var time_str = date.getHours() + ":" + date.getMinutes();
		var time_class = "span-" + time_str.replace(":", "");
		if (class_value) {
			msg_li.find("." + assistant.refs.msgBodyClass)
					.addClass(class_value);
		}
		msg_li.find("." + assistant.refs.msgHeaderClass).attr("src", img_value);
		if ($("." + time_class).length == 0) {
			msg_li.find("." + assistant.refs.msgTimeClass + " span").attr(
					"class", time_class).html(time_str);
		}
		msg_li.find("." + assistant.refs.msgContentClass).html(msg);

		assistant.bindChoiceEvent(msg_li);
		assistant.bindMediaEvent(msg_li);

		$("#" + assistant.refs.containerId + " ul").append(msg_li);

		msg_li.show();

		$(window).resize();

		$("#" + assistant.refs.contentInputId).val("");
	}

	assistant.sendData = function() {
		var input_content = $("#" + assistant.refs.contentInputId).val();
		if (!input_content) {
			JavascriptInterface.showMsg("Please input your words!");
			return false;
		}
		$("#" + assistant.refs.btnSendId).unbind();
		assistant.showMsg(assistant.refs.meUserName, input_content);
		setTimeout(function() {
			var data = JavascriptInterface.getResponse(input_content,
					assistant.requestType.text);
			assistant.showMsg(assistant.refs.assistantUserName, data);
			$("#" + assistant.refs.btnSendId).click(assistant.sendData);
		}, 1100);
	}

	assistant.welcome = function() {
		var data = JavascriptInterface.getResponse("",
				assistant.requestType.text);
		assistant.showMsg(assistant.refs.assistantUserName, data);
	}
}

$(document).ready(function() {
	init();

	$(window).resize(function() {
		assistant.scrollBottom();
	});

	$("#" + assistant.refs.btnSendId).click(assistant.sendData);
	$("#" + assistant.refs.btnBackId).click(assistant.backHome);
	$("#" + assistant.refs.btnLoadId).click(assistant.loadDataFile);

	assistant.welcome();
});
