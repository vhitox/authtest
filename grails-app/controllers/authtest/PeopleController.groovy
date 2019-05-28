package authtest

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

import grails.rest.*
import grails.converters.*


class PeopleController {

    PeopleService peopleService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond peopleService.list(params), model:[peopleCount: peopleService.count()]
    }

    @Secured('ROLE_USER')
    def show(Long id) {
        respond peopleService.get(id)
    }

    def save(People people) {
        if (people == null) {
            render status: NOT_FOUND
            return
        }

        try {
            peopleService.save(people)
        } catch (ValidationException e) {
            respond people.errors, view:'create'
            return
        }

        respond people, [status: CREATED, view:"show"]
    }

    def update(People people) {
        if (people == null) {
            render status: NOT_FOUND
            return
        }

        try {
            peopleService.save(people)
        } catch (ValidationException e) {
            respond people.errors, view:'edit'
            return
        }

        respond people, [status: OK, view:"show"]
    }

    def delete(Long id) {
        if (id == null) {
            render status: NOT_FOUND
            return
        }

        peopleService.delete(id)

        render status: NO_CONTENT
    }
}
