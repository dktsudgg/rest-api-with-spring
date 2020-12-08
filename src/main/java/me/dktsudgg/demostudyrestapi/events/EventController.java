package me.dktsudgg.demostudyrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {  // @Valid 애노테이션은 데이터 바인딩 될 때 값 검증.. 검증 대상 프로퍼티는 해당 클래스에서 애노테이션으로 명세
        /**
         * JSR-303으로 데이터 바인딩 검증
         */
        if (errors.hasErrors()) {
            /**
             * body에 넣기 위해서는 Errors가 ObjectMapper의 BeanSerializer를 통해 직렬화가 되지 않기 때문에(javaBean스펙을 준수하지 않아서)
             * Serializer를 만들어서 ObjectMapper에 등록한 뒤 사용함.. ErrorsSerializer구현..
             */
            return ResponseEntity.badRequest().body(errors);//.badRequest().build();
        }

        /**
         * 데이터 자체를 로직적으로 검증
         */
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);//.badRequest().build();
        }

        // 직접 dto->class로 매핑작업을 작성해도 되지만, ModelMapper 라이브러리를 사용하여 EventDto -> Event Class로 매핑할 수 있음.
        // ModelMapper는 공용으로 쓸 수 있기 때문에 Application에 빈으로 등록해놓고 사용..
        Event event = modelMapper.map(eventDto, Event.class);

        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
    }

}
