package br.ifsp.virtual_studies.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.message.MessageResponseDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.thanks.ThanksPatchDTO;
import br.ifsp.virtual_studies.dto.thanks.ThanksRequestDTO;
import br.ifsp.virtual_studies.dto.thanks.ThanksResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Message;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Thanks;
import br.ifsp.virtual_studies.model.User;
import br.ifsp.virtual_studies.repository.MessageRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.ThanksRepository;
import jakarta.transaction.Transactional;

@Service
public class ThanksService {
    private final MessageRepository messageRepository;
        private final StudentRepository studentRepository;
    private final ThanksRepository thanksRepository;
    private final GamificationClient gamificationClient;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;

    public ThanksService(ThanksRepository thanksRepository, ModelMapper modelMapper,
            PagedResponseMapper pagedResponseMapper, MessageRepository messageRepository, StudentRepository studentRepository, GamificationClient gamificationClient) {
        this.messageRepository = messageRepository;
        this.studentRepository = studentRepository;
        this.thanksRepository = thanksRepository;
        this.gamificationClient = gamificationClient;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }

    @Transactional
    public MessageResponseDTO thankOrUnthankMessage(Long idMessage,
            ThanksPatchDTO thanks2,
            User user) {
        Message message = messageRepository.findById(idMessage)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Message not found with ID: " + idMessage));
        Chat chat = message.getChat();
        Student thanker = studentRepository.findById(user.getId())
                .orElse(null);
        if (!chat.containsStudent(thanker)) {
            throw new AccessDeniedException("Access Denied");
        }
        if (!message.studentAlreadyThanked(thanker)) {
            Thanks thanks = new Thanks();
            thanks.setMessage(message);
            thanks.setStudent(thanker);
            thanks.setCreatedAt(LocalDateTime.now());
            Thanks savedThanks = thanksRepository.save(thanks);
            message.addThanks(savedThanks);
            thankStudent(message.getAuthor(), +10);
        } else {
            Thanks thanks = message.removeThanks(thanker);
            thanksRepository.delete(thanks);
            thankStudent(message.getAuthor(), -10);
        }
        Message updatedMessage = messageRepository.save(message);
        return modelMapper.map(updatedMessage, MessageResponseDTO.class);
    }

    private void thankStudent(User user, int score) {
        if (user.getClass().equals(Student.class)) {
            Student student = studentRepository.findById(user.getId())
                    .orElse(null);
            gamificationClient.addPoints(student.getId(), score);
        }
    }
}