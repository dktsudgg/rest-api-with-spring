package me.dktsudgg.demostudyrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//public class EventResource extends RepresentationModel {
//
//    // json으로 변환 시 event정보가 한단계 하위 레벨에 생성되는 것을 막기위한 애노테이션
//    @JsonUnwrapped
//    private Event event;
//
//    public EventResource(Event event) {
//        this.event = event;
////        add(new Link("http://localhost:8080/api/events/" + event.getId()));
//        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
//    }
//
//    public Event getEvent() {
//        return event;
//    }
//}

// 이렇게 작성하면 코드 간결..
public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links) {
        super(event, links);
//        add(new Link("http://localhost:8080/api/events/" + event.getId()));
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
