package br.ifsp.virtual_studies.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.student.StudentRequestDTO;
import br.ifsp.virtual_studies.dto.thanks.ThanksPatchDTO;
import br.ifsp.virtual_studies.dto.thanks.ThanksRequestDTO;
import br.ifsp.virtual_studies.dto.answer.AnswerPatchDTO;
import br.ifsp.virtual_studies.dto.answer.AnswerRequestDTO;
import br.ifsp.virtual_studies.dto.answer.AnswerResponseDTO;
import br.ifsp.virtual_studies.dto.chat.ChatPatchDTO;
import br.ifsp.virtual_studies.dto.chat.ChatRequestDTO;
import br.ifsp.virtual_studies.dto.chat.ChatResponseDTO;
import br.ifsp.virtual_studies.dto.exercise.ExercisePatchDTO;
import br.ifsp.virtual_studies.dto.exercise.ExerciseRequestDTO;
import br.ifsp.virtual_studies.dto.exercise.ExerciseResponseDTO;
import br.ifsp.virtual_studies.dto.material.MaterialPatchDTO;
import br.ifsp.virtual_studies.dto.material.MaterialRequestDTO;
import br.ifsp.virtual_studies.dto.material.MaterialResponseDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingPatchDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingRequestDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingResponseDTO;
import br.ifsp.virtual_studies.dto.message.MessageRequestDTO;
import br.ifsp.virtual_studies.dto.message.MessageResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Answer;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Exercise;
import br.ifsp.virtual_studies.model.Material;
import br.ifsp.virtual_studies.model.Meeting;
import br.ifsp.virtual_studies.model.Message;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.model.Thanks;
import br.ifsp.virtual_studies.model.Usuario;
import br.ifsp.virtual_studies.repository.AnswerRepository;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.ExerciseRepository;
import br.ifsp.virtual_studies.repository.MaterialRepository;
import br.ifsp.virtual_studies.repository.MeetingRepository;
import br.ifsp.virtual_studies.repository.MessageRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;
import br.ifsp.virtual_studies.repository.ThanksRepository;
import br.ifsp.virtual_studies.repository.UsuarioRepository;
import jakarta.validation.Valid;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final AnswerRepository answerRepository;
    private final ExerciseRepository exerciseRepository;
    private final MaterialRepository materialRepository;
    private final MeetingRepository meetingRepository;
    private final MessageRepository messageRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UsuarioRepository usuarioRepository;
    private final ThanksRepository thanksRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public ChatService(ChatRepository chatRepository, TeacherRepository teacherRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper, StudentRepository studentRepository, UsuarioRepository usuarioRepository, MessageRepository messageRepository, ThanksRepository thanksRepository, MaterialRepository materialRepository, ExerciseRepository exerciseRepository, AnswerRepository answerRepository, MeetingRepository meetingRepository) {
        this.chatRepository = chatRepository;
        this.answerRepository = answerRepository;
        this.exerciseRepository = exerciseRepository;
        this.materialRepository = materialRepository;
        this.meetingRepository = meetingRepository;
        this.messageRepository = messageRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.usuarioRepository = usuarioRepository;
        this.thanksRepository = thanksRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public ChatResponseDTO createChat(ChatRequestDTO chatDto, Usuario usuario) {
        Teacher teacher = teacherRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        Chat chat = new Chat();
        chat.setSubject(chatDto.getSubject());
        chat.setTeacher(teacher);
        chat.setCreatedAt(LocalDateTime.now());
        Chat createdChat = chatRepository.save(chat);
        return modelMapper.map(createdChat, ChatResponseDTO.class);
    }
    
    public PagedResponse<ChatResponseDTO> getTeacherChats(Usuario usuario, Pageable pageable) {
        Teacher teacher = teacherRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        Page<Chat> chatsPage = chatRepository.findByTeacher(teacher, pageable);
        return pagedResponseMapper.toPagedResponse(chatsPage, ChatResponseDTO.class);
    }
    
    public ChatResponseDTO getChatById(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        return modelMapper.map(chat, ChatResponseDTO.class);
    }
    
    public ChatResponseDTO updateChat(Long id, ChatPatchDTO chatDto) {
        Chat existingChat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + id));
        if (chatDto.getSubject().isPresent()) existingChat.setSubject(chatDto.getSubject().get());
        Chat updatedChat = chatRepository.save(existingChat);
        return modelMapper.map(updatedChat, ChatResponseDTO.class);
    }

    public ChatResponseDTO assignOrUnassignStudent(Long idChat, Long idStudent, Usuario usuario) {
        Teacher teacher = teacherRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        if (!teacher.equals(chat.getTeacher())) {
            throw new AccessDeniedException("Access Denied");
        }
        Student existingStudent = studentRepository.findById(idStudent)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + idStudent));
        if (!chat.containsStudent(existingStudent)) {
            chat.addStudent(existingStudent);
        } else {
            chat.removeStudent(existingStudent);
        }
        Chat updatedChat = chatRepository.save(chat);
        return modelMapper.map(updatedChat, ChatResponseDTO.class);
    }

    public MessageResponseDTO createOrDeleteMessage(Long idChat, MessageRequestDTO messageDto, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Long authorId = usuario.getId();
        Usuario author = usuarioRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + authorId));
        Student studentPossibleUser = studentRepository.findById(authorId)
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(authorId)
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Message message = new Message();
        message.setChat(chat);
        message.setAuthor(author);
        message.setText(messageDto.getText());
        message.setCreatedAt(LocalDateTime.now());
        Message newMessage = messageRepository.save(message);
        return modelMapper.map(newMessage, MessageResponseDTO.class);
    }

    public PagedResponse<MessageResponseDTO> getMessages(Long idChat, Usuario usuario, Pageable pageable) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Page<Message> messagesPage = messageRepository.findByChat(chat, pageable);
        return pagedResponseMapper.toPagedResponse(messagesPage, MessageResponseDTO.class);
    }

    public MessageResponseDTO thankOrUnthankMessage(Long idChat, Long idMessage, ThanksPatchDTO thanks2, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Message message = messageRepository.findById(idMessage)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with ID: " + idMessage));
        if (!chat.containsMessage(message)) {
            throw new ResourceNotFoundException("Message not found with ID: " + idMessage);
        }
        Student thanker = studentRepository.findById(usuario.getId())
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
            thankStudent(message.getAuthor(), 10);
        } else {
            Thanks thanks = message.removeThanks(thanker);
            thanksRepository.delete(thanks);
            thankStudent(message.getAuthor(), -10);
        }
        Message updatedMessage = messageRepository.save(message);
        return modelMapper.map(updatedMessage, MessageResponseDTO.class);
    }

    private void thankStudent(Usuario usuario, int score) {
        if (usuario.getClass().equals(Student.class)) {
            Student student = studentRepository.findById(usuario.getId())
                    .orElse(null);
            student.updateScore(score);
            studentRepository.save(student);
        }
    }

    public MaterialResponseDTO createMaterial(Long idChat, MaterialRequestDTO materialDto, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Material material = new Material();
        material.setChat(chat);
        material.setTitle(materialDto.getTitle());
        material.setDescription(materialDto.getDescription());
        material.setLocal(materialDto.getLocal());
        material.setCreatedAt(LocalDateTime.now());
        Material savedMaterial = materialRepository.save(material);
        return modelMapper.map(savedMaterial, MaterialResponseDTO.class);
    }

    public PagedResponse<MaterialResponseDTO> listMaterials(Long idChat, Usuario usuario, Pageable pageable) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Page<Material> materialsPage = materialRepository.findByChat(chat, pageable);
        return pagedResponseMapper.toPagedResponse(materialsPage, MaterialResponseDTO.class);
    }

    public MaterialResponseDTO getMaterial(Long idChat, Long idMaterial, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Material material = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with ID: " + idMaterial));
        if (!chat.containsMaterial(material)) {
            throw new ResourceNotFoundException("Material not found with ID: " + idMaterial);
        }
        return modelMapper.map(material, MaterialResponseDTO.class);
    }

    public MaterialResponseDTO updateMaterial(Long idChat, Long idMaterial, MaterialPatchDTO materialDto, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Material existingMaterial = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with ID: " + idMaterial));
        if (!chat.equals(existingMaterial.getChat())) {
            throw new ResourceNotFoundException("Material not found with ID: " + idMaterial);
        }
        if (materialDto.getTitle().isPresent()) existingMaterial.setTitle(materialDto.getTitle().get());
        if (materialDto.getDescription().isPresent()) existingMaterial.setDescription(materialDto.getDescription().get());
        if (materialDto.getLocal().isPresent()) existingMaterial.setLocal(materialDto.getLocal().get());
        Material updatedMaterial = materialRepository.save(existingMaterial);
        return modelMapper.map(updatedMaterial, MaterialResponseDTO.class);
    }

    public void deleteMaterial(Long idChat, Long idMaterial, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Material material = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with ID: " + idMaterial));
        if (!chat.equals(material.getChat())) {
            throw new ResourceNotFoundException("Material not found with ID: " + idMaterial);
        }
        materialRepository.delete(material);
    }

    public MeetingResponseDTO createMeeting(Long idChat, MeetingRequestDTO meetingDto, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Meeting meeting = new Meeting();
        meeting.setChat(chat);
        meeting.setTitle(meetingDto.getTitle());
        meeting.setDescription(meetingDto.getDescription());
        meeting.setLink(meetingDto.getLink());
        meeting.setCreatedAt(LocalDateTime.now());
        Meeting savedMeeting = meetingRepository.save(meeting);
        return modelMapper.map(savedMeeting, MeetingResponseDTO.class);
    }

    public PagedResponse<MeetingResponseDTO> listMeetings(Long idChat, Usuario usuario, Pageable pageable) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Page<Meeting> meetingsPage = meetingRepository.findByChat(chat, pageable);
        return pagedResponseMapper.toPagedResponse(meetingsPage, MeetingResponseDTO.class);
    }

    public MeetingResponseDTO getMeeting(Long idChat, Long idMeeting, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Meeting meeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + idMeeting));
        if (!chat.containsMeeting(meeting)) {
            throw new ResourceNotFoundException("Meeting not found with ID: " + idMeeting);
        }
        return modelMapper.map(meeting, MeetingResponseDTO.class);
    }

    public MeetingResponseDTO updateMeeting(Long idChat, Long idMeeting, MeetingPatchDTO meetingDto, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Meeting existingMeeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + idMeeting));
        if (!chat.equals(existingMeeting.getChat())) {
            throw new ResourceNotFoundException("Meeting not found with ID: " + idMeeting);
        }
        if (meetingDto.getTitle().isPresent()) existingMeeting.setTitle(meetingDto.getTitle().get());
        if (meetingDto.getDescription().isPresent()) existingMeeting.setDescription(meetingDto.getDescription().get());
        if (meetingDto.getLink().isPresent()) existingMeeting.setLink(meetingDto.getLink().get());
        if (meetingDto.getClosed().isPresent()) existingMeeting.setClosed(meetingDto.getClosed().get());
        Meeting updatedMeeting = meetingRepository.save(existingMeeting);
        return modelMapper.map(updatedMeeting, MeetingResponseDTO.class);
    }

    public void deleteMeeting(Long idChat, Long idMeeting, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Meeting meeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + idMeeting));
        if (!chat.equals(meeting.getChat())) {
            throw new ResourceNotFoundException("Meeting not found with ID: " + idMeeting);
        }
        meetingRepository.delete(meeting);
    }

    public ExerciseResponseDTO createExercise(Long idChat, ExerciseRequestDTO exerciseDto, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Exercise exercise = new Exercise();
        exercise.setChat(chat);
        exercise.setTitle(exerciseDto.getTitle());
        exercise.setDescription(exerciseDto.getDescription());
        exercise.setLink(exerciseDto.getLink());
        exercise.setCreatedAt(LocalDateTime.now());
        Exercise savedExercise = exerciseRepository.save(exercise);
        return modelMapper.map(savedExercise, ExerciseResponseDTO.class);
    }

    public PagedResponse<ExerciseResponseDTO> listExercises(Long idChat, Usuario usuario, Pageable pageable) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Page<Exercise> exercisesPage = exerciseRepository.findByChat(chat, pageable);
        return pagedResponseMapper.toPagedResponse(exercisesPage, ExerciseResponseDTO.class);
    }

    public ExerciseResponseDTO getExercise(Long idChat, Long idExercise, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Exercise exercise = exerciseRepository.findById(idExercise)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + idExercise));
        if (!chat.containsExercise(exercise)) {
            throw new ResourceNotFoundException("Exercise not found with ID: " + idExercise);
        }
        return modelMapper.map(exercise, ExerciseResponseDTO.class);
    }

    public ExerciseResponseDTO updateExercise(Long idChat, Long idExercise, ExercisePatchDTO exerciseDto, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Exercise exercise = exerciseRepository.findById(idExercise)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + idExercise));
        if (!chat.equals(exercise.getChat())) {
            throw new ResourceNotFoundException("Exercise not found with ID: " + idExercise);
        }
        if (exerciseDto.getTitle().isPresent()) exercise.setTitle(exerciseDto.getTitle().get());
        if (exerciseDto.getDescription().isPresent()) exercise.setDescription(exerciseDto.getDescription().get());
        if (exerciseDto.getLink().isPresent()) exercise.setLink(exerciseDto.getLink().get());
        Exercise updatedExercise = exerciseRepository.save(exercise);
        return modelMapper.map(updatedExercise, ExerciseResponseDTO.class);
    }

    public void deleteExercise(Long idChat, Long idExercise, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student studentPossibleUser = studentRepository.findById(usuario.getId())
                .orElse(null);
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        Exercise exercise = exerciseRepository.findById(idExercise)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + idExercise));
        if (!chat.equals(exercise.getChat())) {
            throw new ResourceNotFoundException("Exercise not found with ID: " + idExercise);
        }
        exerciseRepository.delete(exercise);
    }

    public AnswerResponseDTO createAnswer(Long idChat, Long idExercise, AnswerRequestDTO answerDto, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Student student = studentRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + usuario.getId()));
        if (!chat.containsStudent(student)) {
            throw new AccessDeniedException("Access Denied");
        }
        Exercise exercise = exerciseRepository.findById(idExercise)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + idExercise));
        Answer answer = new Answer();
        answer.setExercise(exercise);
        answer.setStudent(student);
        answer.setGrade(0.0);
        answer.setCreatedAt(LocalDateTime.now());
        if (!exercise.hasStudentAlreadyAnswered(student))
            student.updateScore(answer.getGrade()*10 + 10);
        Answer newAnswer = answerRepository.save(answer);
        return modelMapper.map(newAnswer, AnswerResponseDTO.class);
    }

    public AnswerResponseDTO updateAnswer(Long idChat, Long idExercise, Long idAnswer, AnswerPatchDTO answerDto, Usuario usuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
        Teacher teacher = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!teacher.equals(chat.getTeacher())) {
            throw new AccessDeniedException("Access Denied");
        }
        Exercise exercise = exerciseRepository.findById(idExercise)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + idExercise));
        Answer answer = answerRepository.findById(idAnswer)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with ID: " + idAnswer));
        if (!exercise.equals(answer.getExercise())) {
            throw new ResourceNotFoundException("Answer not found with ID: " + idAnswer);
        }
        if (answerDto.getGrade().isPresent()) {
            double grade = answerDto.getGrade().get();
            answer.setGrade(grade);
            Student student = studentRepository.findById(answer.getStudent().getId())
                .orElse(null);
            student.updateScore(grade*10 + 10);
            studentRepository.save(student);
        }
        return modelMapper.map(answer, AnswerResponseDTO.class);
    }
    
    public void deleteChat(Long id, Usuario usuario) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + id));
        Teacher teacherPossibleUser = teacherRepository.findById(usuario.getId())
                .orElse(null);
        if (!chat.getTeacher().equals(teacherPossibleUser)) {
            throw new AccessDeniedException("Access Denied");
        }
        chatRepository.delete(chat);
    }
}