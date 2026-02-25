package br.ifsp.virtual_studies.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.student.*;
import br.ifsp.virtual_studies.dto.thanks.*;
import br.ifsp.virtual_studies.dto.answer.*;
import br.ifsp.virtual_studies.dto.chat.*;
import br.ifsp.virtual_studies.dto.exercise.*;
import br.ifsp.virtual_studies.dto.material.*;
import br.ifsp.virtual_studies.dto.meeting.*;
import br.ifsp.virtual_studies.dto.message.*;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.*;
import br.ifsp.virtual_studies.repository.*;
import jakarta.transaction.Transactional;

@Service
public class ChatService {
        private final ChatRepository chatRepository;
        private final ExerciseRepository exerciseRepository;
        private final MessageRepository messageRepository;
        private final MeetingRepository meetingRepository;
        private final MaterialRepository materialRepository;
        private final TeacherRepository teacherRepository;
        private final StudentRepository studentRepository;
        private final UserRepository userRepository;
        private final ModelMapper modelMapper;
        private final PagedResponseMapper pagedResponseMapper;

        public ChatService(ChatRepository chatRepository, TeacherRepository teacherRepository, ModelMapper modelMapper,
                        PagedResponseMapper pagedResponseMapper, StudentRepository studentRepository,
                        UserRepository userRepository, MessageRepository messageRepository,
                        MaterialRepository materialRepository, ExerciseRepository exerciseRepository, MeetingRepository meetingRepository) {
                this.chatRepository = chatRepository;
                this.exerciseRepository = exerciseRepository;
                this.messageRepository = messageRepository;
                this.meetingRepository = meetingRepository;
                this.materialRepository = materialRepository;
                this.teacherRepository = teacherRepository;
                this.studentRepository = studentRepository;
                this.userRepository = userRepository;
                this.modelMapper = modelMapper;
                this.pagedResponseMapper = pagedResponseMapper;
        }

        public ChatResponseDTO createChat(ChatRequestDTO chatDto, User user) {
                Optional<Student> optStudent = studentRepository.findById(user.getId());
                optStudent.ifPresent(__ -> {
                        throw new AccessDeniedException("Access Denied");
                });
                Teacher teacher = teacherRepository.findById(user.getId())
                                .orElseThrow(() -> new AccessDeniedException("Access Denied"));
                Chat chat = new Chat();
                chat.setSubject(chatDto.getSubject());
                chat.setTeacher(teacher);
                chat.setCreatedAt(LocalDateTime.now());
                Chat createdChat = chatRepository.save(chat);
                return modelMapper.map(createdChat, ChatResponseDTO.class);
        }

        public PagedResponse<ChatResponseDTO> getTeacherChats(User user, Pageable pageable) {
                Teacher teacher = teacherRepository.findById(user.getId())
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
                if (chatDto.getSubject().isPresent())
                        existingChat.setSubject(chatDto.getSubject().get());
                Chat updatedChat = chatRepository.save(existingChat);
                return modelMapper.map(updatedChat, ChatResponseDTO.class);
        }

        public PagedResponse<StudentResponseDTO> assignOrUnassignStudent(
                        Long idChat, Long idStudent, User user, Pageable pageable) {
                Teacher teacher = teacherRepository.findById(user.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
                Chat chat = chatRepository.findById(idChat)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
                if (!teacher.equals(chat.getTeacher())) {
                        throw new AccessDeniedException("Access Denied");
                }

                Student student = studentRepository.findById(idStudent)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Student not found with ID: " + idStudent));
                if (!student.getChats().contains(chat)) {
                        student.addToChat(chat);
                } else {
                        student.removeFromChat(chat);
                }
                studentRepository.save(student);
                return getChatStudents(chat, pageable);
        }

        public PagedResponse<StudentResponseDTO> getChatStudents(Chat chat, Pageable pageable) {
                Page<Student> students = studentRepository.findByChatsContaining(chat, pageable);
                return pagedResponseMapper.toPagedResponse(students, StudentResponseDTO.class);
        }

        public PagedResponse<MessageResponseDTO> createMessage(Long idChat, MessageRequestDTO messageDto,
                        User user,
                        Pageable pageable) {
                Chat chat = chatRepository.findById(idChat)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
                Long authorId = user.getId();
                User author = userRepository.findById(authorId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User not found with ID: " + authorId));
                Student studentPossibleUser = studentRepository.findById(authorId)
                                .orElse(new Student());
                Teacher teacherPossibleUser = teacherRepository.findById(authorId)
                                .orElse(new Teacher());
                if (!studentPossibleUser.containsChat(chat) && !teacherPossibleUser.containsChat(chat)) {
                        throw new AccessDeniedException("Access Denied");
                }
                Message message = new Message();
                message.setChat(chat);
                message.setAuthor(author);
                message.setText(messageDto.getText());
                message.setCreatedAt(LocalDateTime.now());
                messageRepository.save(message);
                return getChatMessages(chat, pageable);
        }

        public PagedResponse<MessageResponseDTO> getChatMessages(Chat chat, Pageable pageable) {
                Page<Message> messages = messageRepository.findByChat(chat, pageable);
                return pagedResponseMapper.toPagedResponse(messages, MessageResponseDTO.class);
        }

        public PagedResponse<MessageResponseDTO> getMessages(Long idChat, User user, Pageable pageable) {
                Chat chat = chatRepository.findById(idChat)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + idChat));
                Student studentPossibleUser = studentRepository.findById(user.getId())
                                .orElse(null);
                Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                                .orElse(null);
                if (studentPossibleUser == null && teacherPossibleUser == null) {
                        throw new ResourceNotFoundException("User not found with ID: " + user.getId());
                }
                if (!chat.containsStudent(studentPossibleUser) && !chat.getTeacher().equals(teacherPossibleUser)) {
                        throw new AccessDeniedException("Access Denied");
                }
                Page<Message> messagesPage = messageRepository.findByChat(chat, pageable);
                return pagedResponseMapper.toPagedResponse(messagesPage, MessageResponseDTO.class);
        }

        public MaterialResponseDTO createMaterial(Long idChat, MaterialRequestDTO materialDto, User user) {
                Chat chat = chatRepository.findById(idChat)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " +
                                                idChat));
                Student studentPossibleUser = studentRepository.findById(user.getId())
                                .orElse(new Student());
                Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                                .orElse(new Teacher());
                if (!chat.containsStudent(studentPossibleUser) &&
                                !chat.getTeacher().equals(teacherPossibleUser)) {
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

        public PagedResponse<MaterialResponseDTO> listMaterials(Long idChat, User user, Pageable pageable) {
                Chat chat = chatRepository.findById(idChat)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " +
                                                idChat));
                Student studentPossibleUser = studentRepository.findById(user.getId())
                                .orElse(new Student());
                Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                                .orElse(new Teacher());
                if (!chat.containsStudent(studentPossibleUser) &&
                                !chat.getTeacher().equals(teacherPossibleUser)) {
                        throw new AccessDeniedException("Access Denied");
                }
                Page<Material> materialsPage = materialRepository.findByChat(chat, pageable);
                return pagedResponseMapper.toPagedResponse(materialsPage,
                                MaterialResponseDTO.class);
        }

        public MeetingResponseDTO createMeeting(Long idChat, MeetingRequestDTO meetingDto, User user) {
                Chat chat = chatRepository.findById(idChat)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " +
                                                idChat));
                Student studentPossibleUser = studentRepository.findById(user.getId())
                                .orElse(new Student());
                Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                                .orElse(new Teacher());
                if (!chat.containsStudent(studentPossibleUser) &&
                                !chat.getTeacher().equals(teacherPossibleUser)) {
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

        public PagedResponse<MeetingResponseDTO> listMeetings(Long idChat, User user, Pageable pageable) {
                Chat chat = chatRepository.findById(idChat)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " +
                                                idChat));
                Student studentPossibleUser = studentRepository.findById(user.getId())
                                .orElse(new Student());
                Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                                .orElse(new Teacher());
                if (!chat.containsStudent(studentPossibleUser) &&
                                !chat.getTeacher().equals(teacherPossibleUser)) {
                        throw new AccessDeniedException("Access Denied");
                }
                Page<Meeting> meetingsPage = meetingRepository.findByChat(chat, pageable);
                return pagedResponseMapper.toPagedResponse(meetingsPage,
                                MeetingResponseDTO.class);
        }

        public ExerciseResponseDTO createExercise(Long idChat, ExerciseRequestDTO exerciseDto, User user) {
                Chat chat = chatRepository.findById(idChat)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " +
                                                idChat));
                Student studentPossibleUser = studentRepository.findById(user.getId())
                                .orElse(new Student());
                Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                                .orElse(new Teacher());
                if (!chat.containsStudent(studentPossibleUser) &&
                                !chat.getTeacher().equals(teacherPossibleUser)) {
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

        public PagedResponse<ExerciseResponseDTO> listExercises(Long idChat, User user, Pageable pageable) {
                Chat chat = chatRepository.findById(idChat)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " +
                                                idChat));
                Student studentPossibleUser = studentRepository.findById(user.getId())
                                .orElse(new Student());
                Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                                .orElse(new Teacher());
                if (!chat.containsStudent(studentPossibleUser) &&
                                !chat.getTeacher().equals(teacherPossibleUser)) {
                        throw new AccessDeniedException("Access Denied");
                }
                Page<Exercise> exercisesPage = exerciseRepository.findByChat(chat, pageable);
                return pagedResponseMapper.toPagedResponse(exercisesPage,
                                ExerciseResponseDTO.class);
        }

        // @Transactional
        // public AnswerResponseDTO createAnswer(Long idChat, Long idExercise,
        // AnswerRequestDTO answerDto,
        // User user) {
        // Chat chat = chatRepository.findById(idChat)
        // .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " +
        // idChat));
        // Student student = studentRepository.findById(user.getId())
        // .orElseThrow(() -> new ResourceNotFoundException(
        // "Student not found with ID: " + user.getId()));
        // if (!chat.containsStudent(student)) {
        // throw new AccessDeniedException("Access Denied");
        // }
        // Exercise exercise = exerciseRepository.findById(idExercise)
        // .orElseThrow(() -> new ResourceNotFoundException(
        // "Exercise not found with ID: " + idExercise));
        // Answer answer = new Answer();
        // answer.setExercise(exercise);
        // answer.setStudent(student);
        // answer.setGrade(0.0);
        // answer.setCreatedAt(LocalDateTime.now());
        // if (!exercise.hasStudentAlreadyAnswered(student)) {
        // thankStudent(student, (int) (answer.getGrade() * 10 + 10));
        // }
        // Answer newAnswer = answerRepository.save(answer);
        // return modelMapper.map(newAnswer, AnswerResponseDTO.class);
        // }

        // @Transactional
        // public AnswerResponseDTO updateAnswer(Long idChat, Long idExercise, Long
        // idAnswer, AnswerPatchDTO answerDto,
        // User user) {
        // Chat chat = chatRepository.findById(idChat)
        // .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " +
        // idChat));
        // Teacher teacher = teacherRepository.findById(user.getId())
        // .orElse(new Teacher());
        // if (!teacher.equals(chat.getTeacher())) {
        // throw new AccessDeniedException("Access Denied");
        // }
        // Exercise exercise = exerciseRepository.findById(idExercise)
        // .orElseThrow(() -> new ResourceNotFoundException(
        // "Exercise not found with ID: " + idExercise));
        // Answer answer = answerRepository.findById(idAnswer)
        // .orElseThrow(() -> new ResourceNotFoundException(
        // "Answer not found with ID: " + idAnswer));
        // if (!exercise.equals(answer.getExercise())) {
        // throw new ResourceNotFoundException("Answer not found with ID: " + idAnswer);
        // }
        // if (answerDto.getGrade().isPresent()) {
        // double grade = answerDto.getGrade().get();
        // answer.setGrade(grade);
        // Student student = studentRepository.findById(answer.getStudent().getId())
        // .orElse(new Student());
        // thankStudent(student, (int) (grade * 10 + 10));
        // studentRepository.save(student);
        // }
        // return modelMapper.map(answer, AnswerResponseDTO.class);
        // }

        public void deleteChat(Long id, User user) {
                Chat chat = chatRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + id));
                Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                                .orElse(new Teacher());
                if (!chat.getTeacher().equals(teacherPossibleUser)) {
                        throw new AccessDeniedException("Access Denied");
                }
                chatRepository.delete(chat);
        }
}