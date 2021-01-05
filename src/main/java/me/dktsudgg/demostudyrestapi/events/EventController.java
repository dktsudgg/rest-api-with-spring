package me.dktsudgg.demostudyrestapi.events;

import me.dktsudgg.demostudyrestapi.accounts.Account;
import me.dktsudgg.demostudyrestapi.accounts.CurrentUser;
import me.dktsudgg.demostudyrestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto,
                                      Errors errors,
                                      @CurrentUser Account currentUser) {  // @Valid 애노테이션은 데이터 바인딩 될 때 값 검증.. 검증 대상 프로퍼티는 해당 클래스에서 애노테이션으로 명세
        /**
         * JSR-303으로 데이터 바인딩 검증
         */
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        /**
         * 데이터 자체를 로직적으로 검증
         */
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        // 직접 dto->class로 매핑작업을 작성해도 되지만, ModelMapper 라이브러리를 사용하여 EventDto -> Event Class로 매핑할 수 있음.
        // ModelMapper는 공용으로 쓸 수 있기 때문에 Application에 빈으로 등록해놓고 사용..
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setManager(currentUser);
        Event newEvent = this.eventRepository.save(event);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();
        EntityModel<Event> eventResource = EventResource.modelOf(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));  // 프로필 링크 추가
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(
            Pageable pageable,
            PagedResourcesAssembler<Event> assembler,
            @CurrentUser Account currentUser)
    {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User principal = (User) authentication.getPrincipal();

        Page<Event> page = this.eventRepository.findAll(pageable);
        // 두번째 파라메타:: 각각 이벤트를 이벤트 리소스로 변환하여 각각의 이벤트로 가는 링크(self)가 추가되도록 함.
        var pagedResources = assembler.toModel(page, EventResource::modelOf);

        pagedResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));   // 프로필 링크 추가
        if (currentUser != null) {
            pagedResources.add(linkTo(EventController.class).withRel("create-event"));
        }
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id,
                                   @CurrentUser Account currentUser) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        EntityModel<Event> eventResource = EventResource.modelOf(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));   // 프로필 링크 추가
        if (event.getManager().equals(currentUser)) {   // 업데이트 이벤트를 할 수 있는 링크를 매니저일 경우에만 제공해줌..
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }

        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id
            , @RequestBody @Valid EventDto eventDto
            , Errors errors
            , @CurrentUser Account currentUser
    ) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        this.eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event existingEvent = optionalEvent.get();
        if (!existingEvent.getManager().equals(currentUser)) {
            // 인증이 안되어 있는 유저가 접근했다면 401, 인증은 됐으나 해당 리소스를 볼 권한이 없으면 403
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        this.modelMapper.map(eventDto, existingEvent);
        Event savedEvent = this.eventRepository.save(existingEvent);

        EntityModel<Event> eventResource = EventResource.modelOf(savedEvent);
        eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));   // 프로필 링크 추가

        return ResponseEntity.ok(eventResource);
    }

    private ResponseEntity badRequest(Errors errors) {
        /**
         * body에 넣기 위해서는 Errors가 ObjectMapper의 BeanSerializer를 통해 직렬화가 되지 않기 때문에(javaBean스펙을 준수하지 않아서)
         * Serializer를 만들어서 ObjectMapper에 등록한 뒤 사용함.. ErrorsSerializer구현..
         */
        return ResponseEntity.badRequest().body(ErrorsResource.modelOf(errors));//.badRequest().build();
    }

}
